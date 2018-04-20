package main;

import service.DBService;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        DBService dbService= new DBService();

        UI ui=new UI(dbService);
        boolean go=true;
        while(go){
            try {
                ui.menu();
                go=ui.function();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
