package com.company;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class ReservationBoundary extends RoomIOBoundary {
    Scanner scan = new Scanner(System.in);
    protected void printMenu()
    {
        printMainTitle("Manage Reservations");
        String [] menuList = {
                "Reserve A Room",
                "Print All Reservations",
                "View Reservations By Guest Name",
                "Cancel Reservation",
                "Update Reservation"
        };
        printMenuList(menuList, "Go back to Main Menu");
        System.out.println();
    }

    public LocalDate getStartDateInput(String requestString)
    {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = null;
        String tempString;
        System.out.println(requestString);
        while (date == null){
            try {
                tempString = scan.next();
                date = LocalDate.parse(tempString, dateFormat);

                if(date.isBefore(LocalDate.now().plusDays(1)))
                {
                    System.out.println("Please key in a date after today(dd/mm/yyyy):");
                    date = null;
                }
            }catch (Exception e)
            {
                //e.printStackTrace();
                System.out.println("Please use the format (dd/mm/yyyy)");
            }
        }
        return date;
    }

    public LocalDate getEndDateInput(String requestString,LocalDate startDate)
    {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = null;
        String tempString;
        System.out.println(requestString);
        while (date == null){
            try {
                tempString = scan.next();
                date = LocalDate.parse(tempString, dateFormat);
                if(date.isBefore(startDate))
                {
                    System.out.println("Please key in a date after the end date");
                    date = null;
                }
                if(date.equals(startDate))
                {
                    System.out.println("Please book at least one day");
                    date = null;
                }
            }catch (Exception e)
            {
                //e.printStackTrace();
                System.out.println("Please use the format 'dd/mm/yyyy' for end date");
            }
        }
        return date;
    }

    public void printReservations(ArrayList<ReservationEntity> arrayList, Map<Integer,String> guestNames)
    {
        printMainTitle("All reservations");
        StringBuilder waitListRoomIDs = new StringBuilder();
        String waitListRoomIDsString = "";
        for (ReservationEntity reservationEntity : arrayList) {
            if(reservationEntity.getWaitListRoomIds()!=null) {
                for (String waitListRoomID : reservationEntity.getWaitListRoomIds()) {
                    waitListRoomIDs.append(waitListRoomID).append(",");
                }
                if(waitListRoomIDs.length()!=0)
                    waitListRoomIDsString = waitListRoomIDs.substring(0,waitListRoomIDs.length()-1);
            }

            System.out.println(String.format("[Reservation ID]: %d \n" +
                            "Guest Name: %s \n" +
                            "Number of adults: %d \n" +
                            "Number of children: %d \n" +
                            "Room Number: %s \n" +
                            "Wait List Room Ids: %s \n" +
                            "Start Date: %s \n" +
                            "End Date: %s\n" +
                            "Reservation State: %s",
                    reservationEntity.getReservationId(),
                    guestNames.get(reservationEntity.getGuestId()),
                    reservationEntity.getNumOfAdults(),
                    reservationEntity.getNumOfChildren(),
                    reservationEntity.getRoomId(),
                    waitListRoomIDsString,
                    reservationEntity.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    reservationEntity.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    reservationEntity.getReservationState()
            ));
            printDivider();
        }
    }

    public void printReservationCancellationFailed()
    {
        printDivider();
        System.out.println("Failed to cancel reservation");
    }

    public void printReservationCancelled()
    {
        printDivider();
        System.out.println("The reservation has been cancelled");
    }

    public boolean printNoAvailableRooms()
    {
        printDivider();
        System.out.println("There are no available rooms\nWould you like to be wait listed?(Y/N)");
        String decision;
        do {
            decision = scan.next();
            decision = decision.toUpperCase();
        }while (!decision.equals("Y") && !decision.equals("N"));
        return decision.equals("Y");
    }

    public void printRoomHasBeenReserved(ReservationEntity reservationEntity)
    {
        printDivider();
        //System.out.println(String.format("The room %s has been reserved",roomID));
        //System.out.println(String.format("your reservation Id is: %d",reserveId));
        System.out.println(String.format("[Reservation ID]: %d \n" +
                        "Guest Name: %s \n" +
                        "Number of adults: %d \n" +
                        "Number of children: %d \n" +
                        "Room Number: %s \n" +
                        "Start Date: %s \n" +
                        "End Date: %s\n" +
                        "Reservation State: %s",
                reservationEntity.getReservationId(),
                new GuestController().searchGuest(reservationEntity.getGuestId()).getName(),
                reservationEntity.getNumOfAdults(),
                reservationEntity.getNumOfChildren(),
                reservationEntity.getRoomId(),
                reservationEntity.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                reservationEntity.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                reservationEntity.getReservationState()
        ));
        printDivider();
    }

    public int getReservation(ArrayList<ReservationEntity> arrayList, int guestId,String guestName)
    {
        printMainTitle("Reservations for "+guestName);
        if (arrayList.size() == 0)
        {
            System.out.println("This Guest has no reservations");
        }
        else
            for (ReservationEntity reservationEntity : arrayList) {
                if (reservationEntity.getGuestId() == guestId && reservationEntity.getReservationState() == ReservationEntity.ReservationState.CONFIRMED) {
                    System.out.println(String.format("[Reservation ID]: %d \n" +
                                    "Room Number: %s \n" +
                                    "Start Date: %s \n" +
                                    "End Date: %s\n" +
                                    "Reservation State: %s",
                            reservationEntity.getReservationId(),
                            reservationEntity.getRoomId(),
                            reservationEntity.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            reservationEntity.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            reservationEntity.getReservationState()
                    ));
                }
            }
            return arrayList.size();
    }

}
