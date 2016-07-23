package types;

public enum PorkAction {
	
	invalid,   // initial value. 
	
	//--- client send to server
	ready,
	firstHand,
	addPork,
	believe,
	unbelieve,
	
	giveUp,
	wantEqual,
	agreeEqual,
	refuseEqual,
	
	//--- server send to client
	
	initSend,  // server shuffle the pork and tell who send first.
	initWait,
	
	PorkTransmit,
	
	believeSend,
		
	unbelieveWin,
	unbelieveFail,
	
	giveupWin,
	checkEqual,
	agreeEqualRsp,
	refuseEqualRsp;
}
