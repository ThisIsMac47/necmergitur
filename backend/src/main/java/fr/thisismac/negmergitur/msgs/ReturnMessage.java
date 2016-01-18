package fr.thisismac.negmergitur.msgs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ReturnMessage {
	public boolean 		status;
	public Object	 	message;
}
