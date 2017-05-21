package gmbh.norisknofun.network;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import gmbh.norisknofun.network.socket.SocketSelector;
import gmbh.norisknofun.network.socket.TCPClientSocket;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SessionImpl
 */
public class SessionImplTests {

    private SocketSelector selectorMock;
    private TCPClientSocket socketMock;

    @Before
    public void setUp() {

        selectorMock = mock(SocketSelector.class);
        socketMock = mock(TCPClientSocket.class);
    }

    @Test
    public void aNewlyCreatedSessionIsOpened() {

        // given
        SessionImpl target = new SessionImpl(null);

        // when/then
        assertThat(target.isOpen(), is(true));
        assertThat(target.isClosed(), is(false));
        assertThat(target.isTerminated(), is(false));
    }

    @Test
    public void readingFromANewlyCreatedSessionGivesNull() {

        // given
        SessionImpl target = new SessionImpl(null);

        // when/then
        assertThat(target.read(), is(nullValue()));
    }

    @Test
    public void aNewlyCreatedSessionHasNoDataToWrite() {

        // given
        SessionImpl target = new SessionImpl(null);

        // when/then
        assertThat(target.hasDataToWrite(), is(false));
    }

    @Test(expected = IOException.class)
    public void doReadFromSocketDoesNotCatchExceptionFromSocket() throws IOException {

        // given
        when(socketMock.read(ArgumentMatchers.any(ByteBuffer.class))).thenThrow(IOException.class);
        SessionImpl target = new SessionImpl(null);

        // when/then
        target.doReadFromSocket(socketMock);
    }

    @Test
    public void doReadFromSocketDoesNotPutAnythingIntoInQueueOnError() throws IOException {

        // given
        when(socketMock.read(ArgumentMatchers.any(ByteBuffer.class))).thenReturn(-1);
        SessionImpl target = new SessionImpl(null);

        // when
        int obtained = target.doReadFromSocket(socketMock);

        // then
        assertThat(obtained, is(-1));
        assertThat(target.read(), is(nullValue()));
    }

    @Test
    public void doReadFromSocketDoesNotPutAnythingIntoInQueueWhenNothingWasRead() throws IOException {

        // given
        when(socketMock.read(ArgumentMatchers.any(ByteBuffer.class))).thenReturn(0);
        SessionImpl target = new SessionImpl(null);

        // when
        int obtained = target.doReadFromSocket(socketMock);

        // then
        assertThat(obtained, is(0));
        assertThat(target.read(), is(nullValue()));
    }

    @Test
    public void doReadFromSocketPutsDataIntoInQueueAfterSuccessfulRead() throws IOException {

        // given
        final String message = "Hello World!";

        when(socketMock.read(ArgumentMatchers.any(ByteBuffer.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ByteBuffer data = invocation.getArgument(0);
                data.put(message.getBytes());

                return message.getBytes().length;
            }
        });
        SessionImpl target = new SessionImpl(null);

        // when
        int obtained = target.doReadFromSocket(socketMock);

        // then
        assertThat(obtained, is(message.getBytes().length));
        byte[] readData = target.read();

        assertThat(readData, is(notNullValue()));
        assertThat(readData, is(equalTo(message.getBytes())));
    }

    @Test
    public void readDataIsRemovedFromQueueAfterDataHasBeenConsumed() throws IOException {

        // given
        final String message = "Hello World!";

        when(socketMock.read(ArgumentMatchers.any(ByteBuffer.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ByteBuffer data = invocation.getArgument(0);
                data.put(message.getBytes());

                return message.getBytes().length;
            }
        });
        SessionImpl target = new SessionImpl(null);
        target.doReadFromSocket(socketMock);

        // when reading first time data
        byte[] obtained = target.read();

        // then
        assertThat(obtained, is(notNullValue()));
        assertThat(obtained, is(equalTo(message.getBytes())));

        // and when reading again the data
        obtained = target.read();

        // then
        assertThat(obtained, is(nullValue()));
    }

    @Test
    public void writeWakesUpSelectorIfNoOutgoingDataIsPending() {

        // given
        SessionImpl target = new SessionImpl(selectorMock);

        // when
        target.write("Hello World!".getBytes());

        // then
        verify(selectorMock, times(1)).wakeup();
        assertThat(target.hasDataToWrite(), is(true));
    }

    @Test
    public void writeDoesNotWakeupSelectorIfDataIsAlreadyPresent() {

        // given
        SessionImpl target = new SessionImpl(selectorMock);

        // when
        target.write("Hello ".getBytes());
        target.write("World!".getBytes());

        // then
        verify(selectorMock, times(1)).wakeup();
        assertThat(target.hasDataToWrite(), is(true));
    }

    @Test(expected = IOException.class)
    public void doWriteToSocketDoesNotCatchExceptionFromSocket() throws IOException {

        // given
        when(socketMock.write(ArgumentMatchers.any(ByteBuffer.class))).thenThrow(IOException.class);
        SessionImpl target = new SessionImpl(selectorMock);
        target.write("Hello World!".getBytes());

        // when/then
        target.doWriteToSocket(socketMock);
    }

    @Test
    public void doWriteToSocketReturnsImmediatelyIfNoDataWasWrittenToSocket() throws IOException {

        // given
        when(socketMock.write(ArgumentMatchers.any(ByteBuffer.class))).thenReturn(0);
        SessionImpl target = new SessionImpl(selectorMock);
        target.write("Hello World!".getBytes());

        // when
        int obtained = target.doWriteToSocket(socketMock);

        // then
        assertThat(obtained, is(0));
        assertThat(target.hasDataToWrite(), is(true));
    }

    @Test
    public void doWriteToSocketReturnsNumberOfBytesWrittenAndAdjustsInternalBufferAccordingly() throws IOException {

        // given
        final List<byte[]> consumedData = new LinkedList<>();
        when(socketMock.write(ArgumentMatchers.any(ByteBuffer.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = new byte[2];
                ((ByteBuffer)invocation.getArgument(0)).get(data);
                consumedData.add(data);
                return data.length;
            }
        });

        SessionImpl target = new SessionImpl(selectorMock);
        target.write(new byte[]{ (byte)0xde, (byte)0xad, (byte)0xbe, (byte)0xef });

        // when checking first time if data is available, then
        assertThat(target.hasDataToWrite(), is(true));

        // when sending data to socket
        int obtained = target.doWriteToSocket(socketMock);

        // then
        assertThat(obtained, is(2));
        assertThat(consumedData.size(), is(1));
        assertThat(target.hasDataToWrite(), is(true));

        // when sending data to socket again
        obtained = target.doWriteToSocket(socketMock);

        // then
        assertThat(obtained, is(2));
        assertThat(consumedData.size(), is(2));
        assertThat(target.hasDataToWrite(), is(false));

        // verify the data that was consumed
        assertThat(consumedData.get(0), is(equalTo(new byte[] { (byte)0xde, (byte)0xad })));
        assertThat(consumedData.get(1), is(equalTo(new byte[] { (byte)0xbe, (byte)0xef })));
    }

    @Test
    public void closingSessionSetsStateAccordingly() {

        // given
        SessionImpl target = new SessionImpl(selectorMock);

        // when closing session
        target.close();

        // then check session state
        assertThat(target.isOpen(), is(false));
        assertThat(target.isClosed(), is(true));
        assertThat(target.isTerminated(), is(false));
    }

    @Test
    public void closingSessionWakesUpSelectorOnlyOnce() {

        // given
        SessionImpl target = new SessionImpl(selectorMock);

        // when closing session
        target.close();

        // then verify selector is called
        verify(selectorMock, times(1)).wakeup();

        // and when closing session again
        target.close();

        // then verify selector is not called again
        verify(selectorMock, times(1)).wakeup();
    }

    @Test
    public void afterSessionIsClosedNoDataCanBeWrittenToSession() {

        // given
        SessionImpl target = new SessionImpl(selectorMock);
        target.close();

        // when writing data to the closed session
        target.write("Hello World!".getBytes());

        // then
        assertThat(target.hasDataToWrite(), is(false));
    }

    @Test
    public void closingSessionDoesNotDiscardPendingData() throws IOException {

        // given
        SessionImpl target = new SessionImpl(selectorMock);
        target.write("Hello ".getBytes());
        target.close();

        // then
        assertThat(target.hasDataToWrite(), is(true));

        // and when performing a socket write
        target.doWriteToSocket(socketMock);

        // then
        assertThat(target.hasDataToWrite(), is(true));
    }

    @Test
    public void aClosedSessionDoesNotStoreReadDataFromSocket() throws IOException {

        // given
        final String message = "Hello World!";

        when(socketMock.read(ArgumentMatchers.any(ByteBuffer.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ByteBuffer data = invocation.getArgument(0);
                data.put(message.getBytes());

                return message.getBytes().length;
            }
        });
        SessionImpl target = new SessionImpl(selectorMock);
        target.close();

        // when
        int obtained = target.doReadFromSocket(socketMock);

        // then
        assertThat(obtained, is(0));
        assertThat(target.read(), is(nullValue()));
    }

    @Test
    public void terminatingSessionSetsStateAccordingly() {

        // given
        SessionImpl target = new SessionImpl(selectorMock);

        // when terminating session
        target.terminate();

        // then check session state
        assertThat(target.isOpen(), is(false));
        assertThat(target.isClosed(), is(false));
        assertThat(target.isTerminated(), is(true));
    }

    @Test
    public void terminatingASessionWakesUpSelectorOnlyOnce() {

        // given
        SessionImpl target = new SessionImpl(selectorMock);

        // when terminating session
        target.terminate();

        // then selector is woken up
        verify(selectorMock, times(1)).wakeup();

        // and when terminating an already terminated session again
        target.terminate();

        // then select is not woken up again
        verify(selectorMock, times(1)).wakeup();
    }

    @Test
    public void aClosedSessionCanBeTerminated() {

        // given
        SessionImpl target = new SessionImpl(selectorMock);
        target.close(); // will wakeup selector

        // when terminating session
        target.terminate();

        // then selector is woken up twice (once by close, once by terminate)
        assertThat(target.isTerminated(), is(true));
        verify(selectorMock, times(2)).wakeup();
    }

    @Test
    public void aTerminatedSessionCannotBeClosed() {

        // given
        SessionImpl target = new SessionImpl(selectorMock);
        target.terminate(); // will wakeup selector

        // when closing terminated session
        target.close();

        // then selector is woken up once only by terminated
        verify(selectorMock, times(1)).wakeup();

        // and session state is still terminated
        assertThat(target.isTerminated(), is(true));
        assertThat(target.isClosed(), is(false));
    }

    @Test
    public void afterSessionIsTerminatedNoDataCanBeWrittenToSession() {

        // given
        SessionImpl target = new SessionImpl(selectorMock);
        target.terminate();

        // when writing data to the closed session
        target.write("Hello World!".getBytes());

        // then
        assertThat(target.hasDataToWrite(), is(false));
    }

    @Test
    public void terminatingSessionDoesDiscardPendingData() throws IOException {

        // given
        SessionImpl target = new SessionImpl(selectorMock);
        target.write("Hello ".getBytes());
        target.write("World!".getBytes());
        target.doWriteToSocket(socketMock);
        target.terminate();

        // then
        assertThat(target.hasDataToWrite(), is(false));
    }

    @Test
    public void aTerminatedSessionDoesNotStoreReadDataFromSocket() throws IOException {

        // given
        final String message = "Hello World!";

        when(socketMock.read(ArgumentMatchers.any(ByteBuffer.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ByteBuffer data = invocation.getArgument(0);
                data.put(message.getBytes());

                return message.getBytes().length;
            }
        });
        SessionImpl target = new SessionImpl(selectorMock);
        target.terminate();

        // when
        int obtained = target.doReadFromSocket(socketMock);

        // then
        assertThat(obtained, is(0));
        assertThat(target.read(), is(nullValue()));
    }

    @Test
    public void aTerminatedSessionDiscardsPendingInputData() throws IOException {

        // given
        final String message = "Hello World!";

        when(socketMock.read(ArgumentMatchers.any(ByteBuffer.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ByteBuffer data = invocation.getArgument(0);
                data.put(message.getBytes());

                return message.getBytes().length;
            }
        });
        SessionImpl target = new SessionImpl(selectorMock);
        target.doReadFromSocket(socketMock); // read data into in queue

        // when
        target.terminate();

        // then
        assertThat(target.read(), is(nullValue()));
    }
}
