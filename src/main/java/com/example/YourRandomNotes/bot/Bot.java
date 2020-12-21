package com.example.YourRandomNotes.bot;

import com.example.YourRandomNotes.entity.Note;
import com.example.YourRandomNotes.entity.User;
import com.example.YourRandomNotes.service.NoteService;
import com.example.YourRandomNotes.service.UserService;
import com.vdurmont.emoji.EmojiParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    private static final String TOKEN = System.getenv("TOKEN");
    private static final String BOT_USERNAME = System.getenv("BOT_USERNAME");

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    private EditMessageText editMessageText = new EditMessageText();
    private DeleteMessage deleteMessage = new DeleteMessage();

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {




        if (update.hasMessage()) {
            SendMessage send = new SendMessage();
            Message message = update.getMessage();
            String chatId = Long.toString(message.getChatId());
            User userInfo = userService.findByChatId(chatId);
            log.info("Message from user: " + chatId + " Text: " + message.getText());

            if (userService.existsByChatId(chatId)) {

                if (userInfo.getState()) {
                    if (message.getText().equals("Отмена")) {
                        setButtons(send);
                        userInfo.setState(false);
                        userService.save(userInfo);
                        send.setChatId(chatId);
                        send.setText("Отменено");
                        execute(send);
                        return;
                    } else {
                        replyKeyboardMarkup.setOneTimeKeyboard(true);
                        setButtons(send);
                        Note note;
                        note = noteService.setNote(message.getText(), message.getMessageId());
                        noteService.save(note);
                        log.info("Save note: {} id: {}", note.getNote(), message.getMessageId());
                        userInfo.setState(false);
                        userService.save(userInfo);
                        send.setChatId(chatId);
                        send.setText("Сохранил");
                        execute(sendInlineKeyBoardMessageDelete(message.getMessageId(), send));
                        execute(send);

                        return;
                    }
                }
                if (message.getText().equals("Добавить заметку") || message.getText().equals("Получить заметку")) {

                    if (message.getText().equals("Добавить заметку")) {
                        setCloseButton(send);
                        send.setChatId(chatId);
                        send.setText("Напиши заметку: ");
                        execute(send);
                        userInfo.setState(true);
                        userService.save(userInfo);

                        execute(deleteMessage.setChatId(chatId)
                                .setMessageId(update.getMessage().getMessageId()));

                    }
                    if (message.getText().equals("Получить заметку")) {
                        send.setChatId(chatId);
                        send.setText("Случайная заметка: " + noteService.getRandomNote());
                        execute(send);
                    }
                } else {
                    replyKeyboardMarkup.setOneTimeKeyboard(true);
                    setButtons(send);
                    send.setChatId(chatId);
                    send.setText("Воспользуйся кнопками");
                    execute(send);
                }

            } else {
                send.setChatId(chatId);
                send.setText("Добро пожаловать в этот мир заметок");
                execute(send);
                setButtons(send);
                User user = new User();
                user.setChatId(chatId);
                user.setState(false);
                log.info("New user: " + chatId);
                userService.save(user);
            }
        } else if (update.hasCallbackQuery()) {

            execute(deleteMessage.setChatId(update.getCallbackQuery().getMessage().getChatId())
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId()));
            execute(deleteMessage.setChatId(update.getCallbackQuery().getMessage().getChatId())
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId() - 1));
            execute(deleteMessage.setChatId(update.getCallbackQuery().getMessage().getChatId())
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId() - 2));

            noteService.delete(update.getCallbackQuery().getMessage().getMessageId() - 1);

            log.info("Delete note {}", update.getCallbackQuery().getMessage().getMessageId() - 1);


        }
    }

    private SendMessage sendInlineKeyBoardMessageDelete(Integer messageId, SendMessage sendMessage) {
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode(":x:")).setCallbackData("Удалить"));

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);


        return new SendMessage().setChatId(sendMessage.getChatId()).setReplyToMessageId(messageId)
                .setText("Удалить?").setReplyMarkup(inlineKeyboardMarkup);

    }

    private void setButtons(SendMessage sendMessage) {
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);


        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();


        keyboardFirstRow.add("Добавить заметку");

        keyboardFirstRow.add("Получить заметку");
        keyboardRows.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

    private void setCloseButton(SendMessage sendMsg) {
        sendMsg.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add("Отмена");

        keyboardRows.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }


    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }


}
