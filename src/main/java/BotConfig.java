import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BotConfig extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return DataBot.USER;
    }

    @Override
    public String getBotToken() {
        return DataBot.TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()){
            var msg = responder(update);
            try{
                execute(msg);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
        }
    }

    private SendMessage responder(Update update){
        var msgText = update.getMessage().getText().toLowerCase();
        var idChat = update.getMessage().getChatId().toString();

        var resposta = "";

        if ("data".equals(msgText)) {
            resposta = getData();
        } else if (msgText.startsWith("hora")) {
            resposta = getHora();
        } else if (msgText.startsWith("ola") || msgText.startsWith("olá") || msgText.startsWith("oi")) {
            resposta = "Skynet na área!!\uD83E\uDD16";
        } else if (msgText.startsWith("quem é você") || msgText.startsWith("quem e voce")) {
            resposta = "Eu sou Skynet, um bot Telegram criado pelo grupo da Fiap Gang of 4!\uD83E\uDD16";
        } else if (msgText.startsWith("/help")) {
            resposta = "Utilize um dos comandos:\nolá\ndata\nhora\nquem é você?";
        } else {
            resposta = "Não entendi o comando... \nDigite /help para ver os comandos disponíveis.";
        }
        return SendMessage.builder()
                .text(resposta)
                .chatId(idChat)
                .build();
    }

    private String getData() {
        var formatter = new SimpleDateFormat("dd/MM/yyyy");
        return "A data atual é: " + formatter.format(new Date());
    }

    private String getHora() {
        var formatter = new SimpleDateFormat("HH:mm:ss");
        return "A hora atual é: " + formatter.format(new Date());
    }

    public static void botInit(){
        try{
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new BotConfig());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
