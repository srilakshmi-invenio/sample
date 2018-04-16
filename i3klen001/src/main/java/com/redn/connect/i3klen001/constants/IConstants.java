package com.redn.connect.i3klen001.constants;

/**
 * @author  Vinay Kumar Thota
 * 
 * This interface contains constants used for this project
 *
 */
public interface IConstants {

	public static final String ENTERPRISE_MESSAGE = "enterpriseMessage";
	public static final String SERVICE_NAME = "AS2";
	
	public static final String VAR_MESSAGE_ID  = "messageId";
	public static final String VAR_RESOURCE_ID  = "resourceId";
	public static final String VAR_MESSAGE_SOURCE  = "messageSource";
	public static final String VAR_MESSAGE_ACTION  = "messageAction";
	public static final String VAR_SERVICE_NAME  = "serviceName";
	public static final String VAR_TARGET_SYSTEM  = "targetSystem";

	public static final String VAR_TARGET_SYSTEM_SEND_FLOW  = "targetSystemSendFlow";
	
	public static final String VAR_ERROR_CODE = "errorCode";
	public static final String VAR_ERROR_DESCRIPTION = "errorDescription";
	
	public static final String BEAN_CONNECT_CONFIG = "connectConfigBean";
	
	public static final String ERROR_CODE_FILTER_UNACCEPTED = "400101100";
	public static final String ERROR_HANDLEOTHERCODE = "400101110";
	public static final String ERROR_TRANSFORMER = "400101120";
	public static final String ERROR_ACTIVEMQ = "400101130";
	public static final String ERROR_CODE_MANDATORY_FIELD_MISSING = "400101100";
}
