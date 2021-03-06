package com.company;

import java.time.LocalDate;
import java.util.ArrayList;

import com.company.TypesOfRooms.BedType;
import com.company.RoomEntity.RoomStatus;
import com.company.TypesOfRooms.RoomType;

public class RoomController extends Controller
{

	private static RoomController instance = null;
	private ArrayList<RoomEntity> roomList = null;
	private RoomReports roomReports = null;
	private String roomFile = "./data/Rooms.ser";

	private RoomBoundary rb;

	//Create only one instance of the object
	@SuppressWarnings ("unchecked")
	private RoomController()
	{
		roomReports = RoomReports.getInstance();
		roomList = (ArrayList<RoomEntity>) fromFile(roomFile);

		if (roomList == null)
		{
			roomList = new ArrayList<>();
			roomList = new RoomFactory().getRoomList();
			saveFile();
		}
		rb = new RoomBoundary();
	}

	public static RoomController getInstance()
	{

		if (instance == null)
		{
			instance = new RoomController();
		}
		return instance;
	}

	//Return the list based on room type
	@SuppressWarnings ("unchecked")
	public <T> ArrayList<T> listRooms(RoomType roomType)
	{
		ArrayList<T> list = new ArrayList<>();
		for (RoomEntity room : roomList)
		{
			if (room.getRoomType() == roomType)
			{
				list.add((T) room);
			}
		}
		return list;
	}

	//Return the list based on room status
	@SuppressWarnings ("unchecked")
	public <T> ArrayList<T> listRooms(RoomStatus status)
	{
		ArrayList<T> list = new ArrayList<>();
		for (RoomEntity room : roomList)
		{
			if (room.getRoomStatus() == status)
			{
				list.add((T) room);
			}
		}
		return list;
	}

	@SuppressWarnings ("unchecked")
	public <T> ArrayList<T> listRooms(RoomStatus status, ArrayList<RoomEntity> l)
	{
		ArrayList<T> list = new ArrayList<>();
		for (RoomEntity room : l)
		{
			if (room.getRoomStatus() == status)
			{
				list.add((T) room);
			}
		}
		return list;
	}

	//Return the list based on room id
	public RoomEntity getRoom(String roomId)
	{
		for (RoomEntity room : roomList)
		{
			if (room.getRoomId().equals(roomId))
			{
				return room;
			}
		}
		return null;
	}

	public RoomEntity getRoom(int guestId)
	{
		for (RoomEntity room : roomList)
		{
			if (room.getGuestId() == guestId)
			{
				return room;
			}
		}
		return null;
	}

	//Return the list based on room id
	public RoomEntity getReservation(int reserveId)
	{
		for (RoomEntity room : roomList)
		{
			if (room.getReserveId() == reserveId)
			{
				return room;
			}
		}
		return null;
	}

	private void addRoom(String id, RoomType roomType, RoomStatus status, BedType bedType, boolean smoking, boolean wifi)
	{
		loadObject(id, roomType, status, bedType, smoking, wifi);
		saveFile();
	}
	
	//Load object into the array list
	private void loadObject(String id, RoomType roomType, RoomStatus status, BedType bedType, boolean smoking, boolean wifi)
	{
		
		RoomEntity rm = new RoomEntity(id, roomType, status, bedType, smoking, wifi);
		roomList.add(rm);
		roomList.sort(null);
	}

	public void deleteRoom(String id)
	{
		Object room = this.getRoom(id);
		if (room == null)
		{
			System.out.println("Room does not exist");
		}
		else
		{
			roomList.remove(room);
			roomList.sort(null);
			saveFile();
		}
	}

	public void saveFile()
	{
		replaceFile(roomList, roomFile);
	}

	public void roomMaintenance(String roomId)
	{
		try
		{
			this.getRoom(roomId).maintenance();
			System.out.println(roomId + " is under maintenance");
			saveFile();
		} catch (Exception e)
		{
			System.out.println("Room does not exist");
		}
	}

	public void checkIn(int guestId, String roomId, LocalDate startDate, LocalDate endDate,int numAdult,int numChild)
	{
		// TODO Auto-generated method stub
		this.getRoom(roomId).checkIn(guestId, startDate, endDate,numAdult,numChild);
		saveFile();
	}

	public void checkOut(String roomId)
	{
		this.getRoom(roomId).checkOut();
		saveFile();
	}

	public void reserve(String roomId, int guestId, int reserveId)
	{
		try
		{
			this.getRoom(roomId).reserve(guestId, reserveId);
			saveFile();
		} catch (Exception e)
		{
			System.out.println("Room does not exist");
		}
	}

	public void changeBedType(String roomId, BedType bedType)
	{
		try
		{
			this.getRoom(roomId).setBedType(bedType);
			System.out.println("Bed Type Changed");
			saveFile();
		} catch (Exception e)
		{
			System.out.println("Room does not exist");
		}
	}

	public void changeSmoking(String roomId, boolean b)
	{
		try
		{
			this.getRoom(roomId).setSmoking(b);
			System.out.println("Smoking Changed");
			saveFile();
		} catch (Exception e)
		{
			System.out.println("Room does not exist");
		}
	}

	public void changeWifi(String roomId, boolean b)
	{
		try
		{
			this.getRoom(roomId).setWIfi(b);
			System.out.println("WIFI Changed");
			saveFile();
		} catch (Exception e)
		{
			System.out.println("Room does not exist");
		}
	}
	
	public boolean isRoomAvailable(String roomId, LocalDate startDateRequest, LocalDate endDateRequest)
    {
        for (RoomEntity room : roomList) {
            //to check if the reservation has any clashes
        	if(room.getRoomId().equals(roomId)&&room.getRoomStatus()!=RoomStatus.VACANT&&
        			(room.getCheckInDate().isBefore(endDateRequest)||
        					room.getCheckInDate().equals(endDateRequest))&&
        			startDateRequest.isBefore(room.getCheckOutDate())) {
        		return false;
        	}
        }
        return true;
    }

	@Override
	public void processMain()
	{
		int sel = rb.process();
		String roomId;
		BedType bedType;
		RoomType roomType;

		boolean b;
		switch (sel)
		{
			case 1: //1 - Add Rooms
				roomId = rb.getRoomId();
				if(getRoom(roomId)!=null) {
					System.out.println("Room already exists");
					break;
				}
				roomType = rb.getRoomType();
				bedType = rb.getBedType();
				System.out.println("Smoking");
				boolean smoking = rb.getBooleanInput();
				System.out.println("WIFI");
				boolean wifi = rb.getBooleanInput();
				this.addRoom(roomId, roomType, RoomStatus.VACANT, bedType, smoking, wifi);
				System.out.println("Room Created "+getRoom(roomId).toString());
				break;

			case 2: //2 - Delete Rooms
				roomId = rb.getRoomId();
				this.deleteRoom(roomId);
				break;

			case 3: //3 - Change room to maintenance
				roomId = rb.getRoomId();
				System.out.println("Set Maintenance");
				b = rb.getBooleanInput();
				if(b) {
					this.roomMaintenance(roomId);
				}else {
					try {
					this.checkOut(roomId);
					System.out.println(roomId + " is not under maintenance");
					}catch(Exception e) {
						System.out.println("Room does not exist");
					}
				}
				break;
			case 4: //4 - Change room bed type
				roomId = rb.getRoomId();
				bedType = rb.getBedType();
				this.changeBedType(roomId, bedType);
				break;
			case 5: //5 - change room smoking
				roomId = rb.getRoomId();
				System.out.println("Smoking");
				b = rb.getBooleanInput();
				this.changeSmoking(roomId, b);
				break;
			case 6: //6 -  change room wifi
				roomId = rb.getRoomId();
				System.out.println("WIFI");
				b = rb.getBooleanInput();
				this.changeWifi(roomId, b);
				break;
			case 7://7 - Find room by room id
				roomId = rb.getRoomId();
				try
				{
					System.out.println(this.getRoom(roomId).toString());
				} catch (Exception e)
				{
					System.out.println("Room does not exist");
				}
				break;
			case 8://8 - Find room by guest
				int guest;
				try {
					guest = new GuestController().searchGuest_Hybrid().getGuestID();
				}catch(Exception e) {
					System.out.println("Guest does not exist");
					break;
				}
				try
				{
					roomId = getRoom(guest).getRoomId();
					System.out.println(this.getRoom(roomId).toString());
				} catch (Exception e)
				{
					System.out.println("Room does not exist");
				}
				break;
			case 9://Show number of guest in hotel
				rb.printNumGuest(roomList);
				break;
			case 0: // 0 - Go Back
				break;

			default:
				rb.invalidInputWarning();
		}
	}

	public void generateReports()
	{
		roomReports.printReports();
	}

	public ArrayList<RoomEntity> filterRooms(int mode)
	{
		return filterRooms(roomList, mode);
	}

	//isRoomSelection - 0   - Administrative Mode
	//                  1   - Only Vacant Rooms (For walk-in check-in)
	//					2   - Reservation Mode
	public ArrayList<RoomEntity> filterRooms(ArrayList<RoomEntity> list, int mode)
	{
		final int FILTER_SIZE = 15;
		boolean[] filter = new boolean[FILTER_SIZE];
		ArrayList<RoomEntity> filteredList = new ArrayList<RoomEntity>();
		int selectionSize = (mode == 0) ? FILTER_SIZE : FILTER_SIZE - 4;

		switch (mode)
		//Room Selection Mode   - Only Vacant Rooms Selected, all other flags initialised false
		{
			case 0: //Administrative Mode   - All flags initialised true
				for (int i = 0; i < FILTER_SIZE; i++)
				{
					filter[i] = true;
				}
				break;

			case 1: //Only Vacant Rooms pre-selected, all other flags initialised false
				for (int i = 0; i < FILTER_SIZE; i++)
				{
					filter[i] = false;
				}
				filter[11] = true;
				break;

			case 2: // Vacant, Occupied, Reserved Rooms pre-selected
				for (int i = 0; i < FILTER_SIZE; i++)
				{
					filter[i] = false;
				}
				filter[11] = true;
				filter[12] = true;
				filter[13] = true;
				break;
		}


		while (true)
		{
			rb.filterRoom(filter, mode != 0);
			int sel = rb.getInput(0, selectionSize);
			if (sel == 0) break;
			else filter[sel - 1] = !filter[sel - 1];
		}

		for (RoomEntity e : list)
		{
			if ((((e.getRoomType() == RoomType.SINGLE) && filter[0]) ||
					((e.getRoomType() == RoomType.DOUBLE) && filter[1]) ||
					((e.getRoomType() == RoomType.DELUXE) && filter[2]))
					&&
					(((e.getBedType() == BedType.SINGLE) && filter[3]) ||
							((e.getBedType() == BedType.DOUBLESINGLE) && filter[4]) ||
							((e.getBedType() == BedType.QUEEN) && filter[5]) ||
							((e.getBedType() == BedType.KING) && filter[6]))
					&&
					((e.isSmoking() && filter[7]) || (!e.isSmoking() && filter[8]))
					&&
					((e.isWifi() && filter[9]) || (!e.isWifi() && filter[10]))
					&&
					((e.isVacant() && filter[11]) ||
							(e.isOccupied() && filter[12]) ||
							(e.isReserved() && filter[13]) ||
							(e.isMaintenance() && filter[14]))
			)
			{
				filteredList.add(e);
			}
		}
		return filteredList;
	}

}
