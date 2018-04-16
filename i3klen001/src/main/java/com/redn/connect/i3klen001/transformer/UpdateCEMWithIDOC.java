package com.redn.connect.i3klen001.transformer;

import org.mule.api.MuleEventContext;
/**
 * @author  Vinay Kumar Thota

 * 
 * This interface contains constants used for this project
 *
 */
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

import com.redn.connect.i3klen001.constants.IConstants;
import com.redn.connect.util.ConnectUtils;
import com.redn.connect.vo.ConnectEnterpriseMessage;

public class UpdateCEMWithIDOC implements Callable {

	private static ConnectUtils connectUtils = new ConnectUtils();

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleMessage muleMessage = eventContext.getMessage();
		ConnectEnterpriseMessage cem = (ConnectEnterpriseMessage) muleMessage.getProperty(IConstants.ENTERPRISE_MESSAGE,
				PropertyScope.INVOCATION);
		Object payload = muleMessage.getPayload();
		String payloadStr = payload.toString();
		connectUtils.updateConnectEnterprsieMessagePayload(cem, payloadStr);
		return cem;
	}

}
