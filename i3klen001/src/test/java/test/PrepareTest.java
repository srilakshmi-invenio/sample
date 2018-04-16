package test;
/**
 * @author  Vinay Kumar Thota
 * 
 * This interface contains cem to test the project
 *
 */
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import com.redn.connect.vo.ConnectEnterpriseMessage;
import com.redn.connect.vo.ConnectEnterpriseMessage.EnterpriseBody;
import com.redn.connect.vo.EnterpriseHeader;

public class PrepareTest implements Callable {

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		// TODO Auto-generated method stub
		
		String idoc=(String)eventContext.getMessage().getPayload();
		ConnectEnterpriseMessage cem=new ConnectEnterpriseMessage();
		EnterpriseBody ebody=new EnterpriseBody();
		EnterpriseHeader eheader=new EnterpriseHeader();
		eheader.setServiceName("i3klen001");
		eheader.setMessageId("1234");
		eheader.setAction("inbound");
		eheader.setMessageSource("lenovo");
		
		JAXBElement<Object> jaxbElement =new JAXBElement<Object>(new QName("payload"), Object.class, idoc);
		ebody.setAny(jaxbElement);
		cem.setEnterpriseBody(ebody);
		cem.setEnterpriseHeader(eheader);
		return cem;
	}
//	public ConnectEnterpriseMessage getEnterpriseMsg(String str) throws IOException {
//
//	
//		// String line = null;
//		// Set edifact xml to EnterpriseMessage body
//		JAXBElement<Object> jaxbElement = new JAXBElement(new QName("payload"), Object.class, str);
//
//		value.setAny(jaxbElement);
//		header.setMessageId("123456");
//		header.setAction("inbound");
//		header.setServiceName("i3klen011");
//		header.setMessageSource("lenovo");
//		msg.setEnterpriseBody(value);
//		msg.setEnterpriseHeader(header);
//		return msg;
//	}

}
