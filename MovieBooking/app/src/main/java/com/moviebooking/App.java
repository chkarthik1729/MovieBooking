package com.moviebooking;

import com.moviebooking.commands.CommandInvoker;
import com.moviebooking.config.ApplicationConfig;
import com.moviebooking.exceptions.NoSuchCommandException;
import com.moviebooking.exceptions.NoSuchDataException;
import com.moviebooking.repositories.data.DataLoader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {

    static Scanner sc = new Scanner(System.in);
    static ApplicationConfig appConfig = new ApplicationConfig();

    public static void main(String[] args) throws NoSuchDataException {
        List<String> commandLineArgs = new LinkedList<>(List.of(args));
        String expectedSequence = "CINEMA-DATA$SCREEN-DATA$SEAT-DATA$CUSTOMER-DATA$MOVIE-DATA$SHOW-DATA";
        String actualSequence = commandLineArgs.stream()
                .map(a -> a.split("=")[0])
                .collect(Collectors.joining("$"));

        if (!expectedSequence.equals(actualSequence)) {
            System.err.println("Incorrect argument sequence");
            return;
        }

        loadData(commandLineArgs);

        var invoker = appConfig.getCommandInvoker();
        while (sc.hasNextLine()) {
            String[] tokens = sc.nextLine().split(" ");
            try {
                invoker.executeCommand(tokens[0], Arrays.asList(tokens));
            } catch (NoSuchCommandException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadData(List<String> commandLineArgs) {
        var dataLoader = appConfig.getDataLoader();

        for(String arg : commandLineArgs) {
            String[] splits = arg.split("=");
            try {
                dataLoader.loadData(splits[0], splits[1]);
            } catch (NoSuchDataException e) {
                e.printStackTrace();
            }
        }
    }
}
