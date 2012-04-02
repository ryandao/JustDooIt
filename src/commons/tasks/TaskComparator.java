package commons.tasks;


import java.util.*;
import commons.timeframes.TimeFrame;

public class TaskComparator implements Comparator<Task> {

	public int compare(Task task1, Task task2) {
		
		int task1Type = task1.getTimeFrame().getType();
		int task2Type = task2.getTimeFrame().getType();
		
		
		if((task1Type == task2Type))
			return 0;
		
		else if(task1Type == TimeFrame.WHENEVER){
			return 1;
		}
		
		else if(task1Type == TimeFrame.BETWEEN && task2Type != TimeFrame.WHENEVER){
			return 1;
		}
		
		else if (task1Type == TimeFrame.BY && task2Type != TimeFrame.WHENEVER && task2Type != TimeFrame.FROM){
			return 1;
		}
		
		else if (task1Type == TimeFrame.BETWEEN && (task2Type == TimeFrame.FIXPOINT)){
			return 1;
		}
		
		else{
			return -1;
		}
	}

}
