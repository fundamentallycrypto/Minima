package org.minima.system.input;

import org.minima.system.Main;
import org.minima.system.input.functions.backup;
import org.minima.system.input.functions.balance;
import org.minima.system.input.functions.coins;
import org.minima.system.input.functions.connect;
import org.minima.system.input.functions.createtoken;
import org.minima.system.input.functions.disconnect;
import org.minima.system.input.functions.gimme50;
import org.minima.system.input.functions.help;
import org.minima.system.input.functions.intro;
import org.minima.system.input.functions.keys;
import org.minima.system.input.functions.minetrans;
import org.minima.system.input.functions.newaddress;
import org.minima.system.input.functions.newscript;
import org.minima.system.input.functions.printchain;
import org.minima.system.input.functions.printtree;
import org.minima.system.input.functions.quit;
import org.minima.system.input.functions.reconnect;
import org.minima.system.input.functions.runscript;
import org.minima.system.input.functions.send;
import org.minima.system.input.functions.status;
import org.minima.system.input.functions.test;
import org.minima.system.input.functions.trace;
import org.minima.system.input.functions.tutorial;
import org.minima.system.input.functions.txpowinfo;
import org.minima.system.input.functions.weblink;
import org.minima.system.input.functions.transfer.exportkey;
import org.minima.system.input.functions.transfer.importkey;
import org.minima.system.input.functions.txns.txncreate;
import org.minima.system.input.functions.txns.txndelete;
import org.minima.system.input.functions.txns.txninput;
import org.minima.system.input.functions.txns.txnlist;
import org.minima.system.input.functions.txns.txnoutput;
import org.minima.system.input.functions.txns.txnpost;
import org.minima.system.input.functions.txns.txnsign;
import org.minima.system.input.functions.txns.txnvalidate;
import org.minima.utils.ResponseStream;
import org.minima.utils.messages.Message;

public abstract class CommandFunction {

	/**
	 * All the command line functions for Minima
	 */
	public static CommandFunction[] ALL_FUNCTIONS = 
		{
			new backup(), new balance(), new connect(), new createtoken(), new disconnect(), new weblink(),
			new gimme50(), new help(), new intro(), new minetrans(), new newaddress(), new coins(), new txpowinfo(), new keys(),
			new newscript(), new printchain(), new printtree(), new quit(),new reconnect(), new runscript(), 
			new send(), new status(), new test(), new trace(), new tutorial(),
			new exportkey(), new importkey(), 
			new txncreate(), new txndelete(), new txninput(), new txnlist(), 
			new txnoutput(), new txnpost(), new txnsign(), new txnvalidate()
		};  
	
	/**
	 * The function name
	 */
	private String mName;
	
	/**
	 * The Main Handler, so the function can access all data
	 */
	private Main mMain;
	
	/**
	 * The help description
	 */
	String mParams;
	String mSimple;
	String mDescription;
	
	/**
	 * Every function has a response stream to output the results
	 */
	ResponseStream mResponse;
	
	/**
	 * Constructor
	 * 
	 * @param zName
	 */
	public CommandFunction(String zName) {
		mName = zName;
		setHelp("","", "");
	}
	
	public Message getResponseMessage(String zMessageType) {
		//Create a new Message
		Message msg = new Message(zMessageType);
	
		//Add the Response Stream
		msg.addObject(InputHandler.INPUT_RESPONSE, mResponse);
		
		return msg;
	}
	
	public void setHelp(String zParams, String zSimple, String zDescription) {
		mParams			= zParams;
		mSimple 		= zSimple;
		mDescription 	= zDescription;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getParams() {
		return mParams;
	}
	
	public String getSimple() {
		return mSimple;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	
	public void setMainHandler(Main zMainHandler) {
		mMain = zMainHandler;
	}
	
	protected Main getMainHandler() {
		return mMain;
	}
	
	public void setResponseStream(ResponseStream zResponse) {
		mResponse = zResponse;
	}
	
	public ResponseStream getResponseStream() {
		return mResponse;
	}
	
	/**
	 * Do the actual function
	 * @param zInput
	 * @throws Exception
	 */
	public abstract void doFunction(String[] zInput) throws Exception;
	
	/**
	 * Return a new copy of this function
	 * @return
	 */
	public abstract CommandFunction getNewFunction();
	
	/**
	 * Return a specific function given the name
	 * 
	 * @param zFunction
	 * @return
	 * @throws MinimaParseException
	 */
	public static CommandFunction getFunction(String zFunction) {
		//Cycle through all the functions - find the right one..
		for(CommandFunction func : CommandFunction.ALL_FUNCTIONS) {
			//Check it..
			if(func.getName().equalsIgnoreCase(zFunction)) {
				return func.getNewFunction();
			}
		}
		
		return null;
	}
}
