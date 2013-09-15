package ca.uwaterloo.y367zhang.a04;

/**
 * Represent one section of a course offered at UW.
 *
 */
public class SectionModel {
	private String sec;
	private String type;
	private String room;
	private boolean[] days = new boolean[7];
	private String startTime;
	private String endTime;
	
	/**
	 * Construct the section from given information.
	 * @param sec Section number, eg. 001
	 * @param type One of {"LEC", "TUT", "LAB"}
	 * @param room The building and room where it's held.  eg MC4020
	 * @param days The days the section meets represented as a string of 0's (doesn't meet) and 1's (does meet).
	 * 		The first day is Monday.
	 * @param startTime The starting time of the section.
	 * @param endTime The ending time of the section.
	 */
	public SectionModel(String sec, String type, String room, String days, String startTime, String endTime) {
		this.sec = sec;
		this.type = type;
		this.room = room;
		this.startTime = startTime;
		this.endTime = endTime;
		
		assert(days.length() == 7);
		for(int i = 0; i<7; i++) {
			this.days[i] = days.charAt(i) == '1'; 
		}
	}

	/** Get the section number. */
	public String getSec() {
		return sec;
	}

	/** Get the section type; one of {LEC, TUT, LAB}. */
	public String getType() {
		return type;
	}

	/** Get the building and room where the section meets. */
	public String getRoom() {
		return room;
	}

	/** Get the days when this section meets. */
	public boolean[] getDays() {
		return days;
	}

	/** Get this section's starting time. */
	public String getStartTime() {
		return startTime;
	}

	/** Get this section's ending time. */
	public String getEndTime() {
		return endTime;
	}

	// Meaningful abbreviations for the days of the week in toString.
	private static String[] dayOfWeek= {"M", "T", "W", "Th", "F", "Sa", "Su"};

	@Override
	/** Print a meaningful represenation of this section. */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Section: " + this.sec + " Location: " + this.room + " Time: ");
		for(int i=0; i<this.days.length; i++) {
			if (this.days[i]) {
				sb.append(SectionModel.dayOfWeek[i]);
			}
		}
		sb.append("\t" + this.startTime + "-" + this.endTime);
		return sb.toString();
	}
	
	
}
