package gmbh.norisknofun.scene;

import com.badlogic.gdx.graphics.Color;

import gmbh.norisknofun.assets.FontDescriptor;
import gmbh.norisknofun.assets.ModalDialogDescriptor;
import gmbh.norisknofun.assets.TextButtonDescriptor;
import gmbh.norisknofun.assets.TextFieldDescriptor;

/**
 * Class storing constants for several asset filenames, like buttons, audio and such.
 */
public class Assets {

    // graphics
    public static final String CREATE_GAME_BUTTON_FILENAME = "img/button_create_game_eng.png";
    public static final String JOIN_GAME_BUTTON_FILENAME = "img/button_join_game_eng.png";
    public static final String BACK_BUTTON_FILENAME = "img/button_back.png";
    public static final String TWO_PLAYERS_BUTTON_FILENAME = "img/button_2_players.png";
    public static final String THREE_PLAYERS_BUTTON_FILENAME = "img/button_3_players.png";
    public static final String FOUR_PLAYERS_BUTTON_FILENAME = "img/button_4_players.png";
    public static final String START_GAME_BUTTON_FILENAME = "img/start_game_button.png";
    public static final String TEXT_BUTTON_FILENAME = "img/button.png";

    // sounds
    public static final String BUTTON_PRESSED_SOUND_FILENAME = "audio/button_pressed.wav";

    // fonts (font descriptors)
    public static final String DEFAULT_FONT_FILENAME = "fonts/DroidSansMono.ttf";
    public static final FontDescriptor FONT_110PX_WHITE_WITH_BORDER = new FontDescriptor.Builder()
            .setFontFilename(DEFAULT_FONT_FILENAME)
            .setFontSize(110)
            .setForegroundColor(Color.WHITE)
            .setBorderWidth(2.0f)
            .build();
    public static final FontDescriptor FONT_36PX_BLACK_NO_BORDER = new FontDescriptor.Builder()
            .setFontFilename(DEFAULT_FONT_FILENAME)
            .setFontSize(60)
            .setForegroundColor(Color.BLACK)
            .setBorderWidth(0.0f)
            .build();
    public static final FontDescriptor FONT_36PX_GRAY_NO_BORDER = new FontDescriptor.Builder()
            .setFontFilename(DEFAULT_FONT_FILENAME)
            .setFontSize(60)
            .setForegroundColor(Color.GRAY)
            .setBorderWidth(0.0f)
            .build();
    public static final FontDescriptor FONT_36PX_WHITE_WITH_BORDER = new FontDescriptor.Builder()
            .setFontFilename(DEFAULT_FONT_FILENAME)
            .setFontSize(36)
            .setForegroundColor(Color.WHITE)
            .setBorderWidth(2.0f)
            .build();
    public static final FontDescriptor FONT_60PX_BLACK_NO_BORDER = new FontDescriptor.Builder()
            .setFontFilename(DEFAULT_FONT_FILENAME)
            .setFontSize(60)
            .setForegroundColor(Color.BLACK)
            .setBorderWidth(0.0f)
            .build();
    public static final FontDescriptor FONT_60PX_RED_WITH_BORDER = new FontDescriptor.Builder()
            .setFontFilename(DEFAULT_FONT_FILENAME)
            .setFontSize(60)
            .setForegroundColor(Color.RED)
            .setBorderWidth(2.0f)
            .build();
    public static final FontDescriptor FONT_60PX_WHITE_WITH_BORDER = new FontDescriptor.Builder()
            .setFontFilename(DEFAULT_FONT_FILENAME)
            .setFontSize(60)
            .setForegroundColor(Color.WHITE)
            .setBorderWidth(2.0f)
            .build();

    // text button (text button descriptors)
    public static final TextButtonDescriptor DEFAULT_TEXT_BUTTON_DESCRIPTOR = new TextButtonDescriptor.Builder()
            .setTextButtonFont(FONT_36PX_BLACK_NO_BORDER)
            .setDownTextureFilename(TEXT_BUTTON_FILENAME)
            .setUpTextureFilename(TEXT_BUTTON_FILENAME)
            .setFontColor(Color.BLACK)
            .setDownFontColor(Color.BLACK)
            .build();
    public static final TextButtonDescriptor DICE_CHEATS_TEXT_BUTTON_DESCRIPTOR = new TextButtonDescriptor.Builder()
            .setTextButtonFont(FONT_36PX_BLACK_NO_BORDER)
            .setDownTextureFilename(TEXT_BUTTON_FILENAME)
            .setUpTextureFilename(TEXT_BUTTON_FILENAME)
            .setFontColor(new Color(0.9f, 0.5f, 0.5f, 1))
            .setDownFontColor(new Color(0, 0.4f, 0, 1))
            .build();

    // text fields (text field descriptors)
    public static final TextFieldDescriptor NAME_TEXT_FIELD_DESCRIPTOR = new TextFieldDescriptor.Builder()
            .setFont(FONT_36PX_BLACK_NO_BORDER)
            .setFontColor(Color.BLACK)
            .setMessageFont(FONT_36PX_GRAY_NO_BORDER)
            .setMessageFontColor(Color.GRAY)
            .setHintText(Texts.NAME_HINT_TEXT)
            .build();
    public static final TextFieldDescriptor IP_ADDRESS_TEXT_FIELD_DESCRIPTOR = new TextFieldDescriptor.Builder()
            .setFont(FONT_36PX_BLACK_NO_BORDER)
            .setFontColor(Color.BLACK)
            .setMessageFont(FONT_36PX_GRAY_NO_BORDER)
            .setMessageFontColor(Color.GRAY)
            .setHintText(Texts.SERVER_IP_HINT_TEXT)
            .build();

    // modal error dialog
    public static final ModalDialogDescriptor ERROR_DIALOG_DESCRIPTOR = new ModalDialogDescriptor.Builder()
            .setBackgroundColor(new Color(0xbfbfbf80))
            .setButtonDescriptor(DEFAULT_TEXT_BUTTON_DESCRIPTOR)
            .setMessageFont(FONT_60PX_RED_WITH_BORDER)
            .setTitle("")
            .setTitleFont(FONT_60PX_BLACK_NO_BORDER)
            .setTitleFontColor(Color.BLACK)
            .build();


    /**
     * Class stores only constants, so forbid to create an instance.
     */
    private Assets() {
    }
}
