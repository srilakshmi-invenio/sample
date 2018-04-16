package com.redn.connect.i3klen001.processor;
/**
 * AUTHOR @ JIGYASA 
 * 
 */
import java.io.IOException;
/**
 * @author  Vinay Kumar Thota
 * 
 * This interface contains constants used for this project
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

import com.redn.connect.processor.connectconfig.ConnectConfiguration;
import com.redn.connect.util.ConnectUtils;
import com.redn.connect.vo.ConnectEnterpriseMessage;
import com.redn.connect.vo.EnterpriseHeader;
import com.redn.connect.vo.ConnectEnterpriseMessage.EnterpriseBody;
import com.redn.connect.zorder.len.ObjectFactory;
import com.redn.connect.zorder.len.ZORD05LEN;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC.E1EDK01;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC.E1EDK01.ZE1EDK01;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC.E1EDK01.ZE1EDKFT01;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC.E1EDK02;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC.E1EDK03;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC.E1EDKA1;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC.E1EDP01;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC.E1EDP01.E1EDP19;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC.E1EDP01.E1EDPA1;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC.E1EDP01.ZE1EDP01;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC.E1EDP01.ZE1EDPFT01;
import com.redn.connect.zorder.len.ZORD05LEN.IDOC.EDIDC40;

public class EdifactToIDocProcessor implements Callable {
	private static ConnectUtils connectUtils = new ConnectUtils();

	@SuppressWarnings("rawtypes")
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {

		MuleMessage message = eventContext.getMessage();
		ConnectConfiguration connectConfig = eventContext.getMuleContext().getRegistry().lookupObject("connectConfigBean");
			

		Map payload = (Map) message.getPayload();

		Map messages = (Map) payload.get("Messages");

		Map D97A = (Map) messages.get("D97A");

		// TODO - Check for Errors and throw an exception

		List orderResponse = (List) D97A.get("ORDERS");

		// List of output IDocs
		List<ZORD05LEN> outputIDocs = new ArrayList<ZORD05LEN>();

		ObjectFactory iDocObjectFactory = new ObjectFactory();

		for (int i = 0; i < orderResponse.size(); i++) {

			// Creates IDoc, sets EDIDC40, E1EDK01/ZE1EDK01 segments
			ZORD05LEN orderResponseIDoc = createZORD05LEN(iDocObjectFactory);
			outputIDocs.add(orderResponseIDoc);
			orderResponseIDoc.getIDOC().setBEGIN("1");

			E1EDK01 e1edk01 = orderResponseIDoc.getIDOC().getE1EDK01();
			e1edk01.setSEGMENT("1");
			ZE1EDK01 ze1edk01 = orderResponseIDoc.getIDOC().getE1EDK01().getZE1EDK01();
			ze1edk01.setSEGMENT("1");
			Map response = (Map) orderResponse.get(i);

			HashMap messageHeader = (HashMap) response.get("MessageHeader");
			String UNH01 = (String) messageHeader.get("UNH01");
			orderResponseIDoc.getIDOC().getEDIDC40().setSEGMENT("1");
			// Set Message Reference Number
			orderResponseIDoc.getIDOC().getEDIDC40().setDOCNUM(UNH01);
			orderResponseIDoc.getIDOC().getEDIDC40().setTABNAM(connectConfig.get("connect.i3klen001.idoc.TABNAM"));
			orderResponseIDoc.getIDOC().getEDIDC40().setMANDT(connectConfig.get("connect.i3klen001.idoc.MANDT"));
			// orderResponseIDoc.getIDOC().getEDIDC40().setDOCNUM("0000000095068502");
			orderResponseIDoc.getIDOC().getEDIDC40().setDOCREL(connectConfig.get("connect.i3klen001.idoc.DOCREL"));
			orderResponseIDoc.getIDOC().getEDIDC40().setSTATUS(connectConfig.get("connect.i3klen001.idoc.STATUS"));
			orderResponseIDoc.getIDOC().getEDIDC40().setDIRECT(connectConfig.get("connect.i3klen001.idoc.DIRECT"));
			orderResponseIDoc.getIDOC().getEDIDC40().setOUTMOD(connectConfig.get("connect.i3klen001.idoc.OUTMOD"));
			orderResponseIDoc.getIDOC().getEDIDC40().setIDOCTYP(connectConfig.get("connect.i3klen001.idoc.IDOCTYP"));
			orderResponseIDoc.getIDOC().getEDIDC40().setMESTYP(connectConfig.get("connect.i3klen001.idoc.MESTYP"));
			orderResponseIDoc.getIDOC().getEDIDC40().setCIMTYP(connectConfig.get("connect.i3klen001.idoc.CIMTYP"));
			
			/*orderResponseIDoc.getIDOC().getEDIDC40().setSNDPOR("A000000018");*///sender portNo
			orderResponseIDoc.getIDOC().getEDIDC40().setSNDPOR(connectConfig.get("connect.i3klen001.idoc.SNDPOR"));
			orderResponseIDoc.getIDOC().getEDIDC40().setSNDPRT(connectConfig.get("connect.i3klen001.idoc.SNDPRT"));// partner type
			/*orderResponseIDoc.getIDOC().getEDIDC40().setSNDPRN("2010000005"); */// partner no 
			orderResponseIDoc.getIDOC().getEDIDC40().setSNDPRN(connectConfig.get("connect.i3klen001.idoc.SNDPRN"));
			
			orderResponseIDoc.getIDOC().getEDIDC40().setRCVPOR(connectConfig.get("connect.i3klen001.idoc.RCVPOR")); //receiver portNo
			
			orderResponseIDoc.getIDOC().getEDIDC40().setRCVPRT(connectConfig.get("connect.i3klen001.idoc.RCVPRT"));
			orderResponseIDoc.getIDOC().getEDIDC40().setRCVPRN(connectConfig.get("connect.i3klen001.idoc.RCVPRN"));
			orderResponseIDoc.getIDOC().getEDIDC40().setMESCOD(connectConfig.get("connect.i3klen001.idoc.MESCOD"));
			orderResponseIDoc.getIDOC().getEDIDC40().setMESFCT(connectConfig.get("connect.i3klen001.idoc.MESFCT"));

			String sapCurrentTimeAsString = ConnectUtils.getSAPCurrentTimeAsString();
			orderResponseIDoc.getIDOC().getEDIDC40().setCREDAT(sapCurrentTimeAsString.substring(0,8)); 
			orderResponseIDoc.getIDOC().getEDIDC40().setCRETIM(sapCurrentTimeAsString.substring(8,14));
			orderResponseIDoc.getIDOC().getEDIDC40().setSERIAL(sapCurrentTimeAsString);
			
			HashMap heading = (HashMap) response.get("Heading");

			HashMap detail = (HashMap) response.get("Detail");
	

			// Set Commited Service Date
			List DTM = (List) heading.get("0030_DTM");
			for (int idtm = 0; idtm < DTM.size(); idtm++) {

				HashMap iDTM = (HashMap) DTM.get(idtm);
				String DTM0101 = (String) iDTM.get("DTM0101");
				String DTM0102 = (String) iDTM.get("DTM0102");
				String DTM0103 = (String) iDTM.get("DTM0103");
				if ("69".equals(DTM0101) && "304".equals(DTM0103)) {
					// Then set E1EDK01/ZE1EDK01 to CSD
					if (DTM0102 != null) {
						int indexOfUTC = DTM0102.indexOf("UTC");
						String CSD = DTM0102;
						if (indexOfUTC != -1) {
							CSD = DTM0102.substring(0, indexOfUTC);
							CSD = CSD.substring(0,CSD.length());
						}
						ze1edk01.setCSD(CSD);
					}
				}
			}

			List FTX = (List) heading.get("0070_FTX");

			if (FTX != null) {

				for (int iftx = 0; iftx < FTX.size(); iftx++) {

					HashMap iFTX = (HashMap) FTX.get(iftx);
					String FTX01 = (String) iFTX.get("FTX01");
					if ("COI".equals(FTX01)) {
						// Set Service Order Type
						// IF FTX/4451="COI" THEN SET
						// E1EDK01-BSART = FTX/C108/4440
						String FTX0401 = (String) iFTX.get("FTX0401");
						e1edk01.setBSART(FTX0401);
						
					} else if ("PRI".equals(FTX01)) {
						// Service Order Priority
						// IF FTX/4451="PRI" THEN SET
						// E1EDK01-BSTZD = FTX/C107/4441
						String FTX0301 = (String) iFTX.get("FTX0301");
						e1edk01.setBSTZD(FTX0301);
					}
					/*
					 * else if("AAP".equals(FTX01)){ //Response Code //IF
					 * FTX/4451="AAP" THEN SET //ZE1EDK01-RESPC = FTX/C108/4440
					 * String FTX0401 = (String) iFTX.get("FTX0401");
					 * ze1edk01.setRESPC(FTX0401);
					 * 
					 * //Response Code Desc //IF FTX/4451="AAP" THEN SET
					 * //ZE1EDKFT01-VAL1 =FTX/C108/4440_2
					 * //ZE1EDKFT01-VAL2=FTX/C108/4440_3
					 * //ZE1EDKFT01-VAL3=FTX/C108/4440_4
					 * //ZE1EDKFT01-VAL4=FTX/C108/4440_5 //AND ZE1EDKFT01-QUAL =
					 * "010" ZE1EDKFT01 zE1EDKFT01_ResCodeDesc =
					 * iDocObjectFactory.createZORD05LENIDOCE1EDK01ZE1EDKFT01();
					 * 
					 * String FTX0402 = (String) iFTX.get("FTX0402");
					 * zE1EDKFT01_ResCodeDesc.setVAL1(FTX0402);
					 * 
					 * String FTX0403 = (String) iFTX.get("FTX0403");
					 * zE1EDKFT01_ResCodeDesc.setVAL2(FTX0403);
					 * 
					 * String FTX0404 = (String) iFTX.get("FTX0404");
					 * zE1EDKFT01_ResCodeDesc.setVAL3(FTX0404);
					 * 
					 * String FTX0405 = (String) iFTX.get("FTX0405");
					 * zE1EDKFT01_ResCodeDesc.setVAL4(FTX0405);
					 * 
					 * zE1EDKFT01_ResCodeDesc.setQUAL("010");
					 * 
					 * e1edk01.getZE1EDKFT01().add(zE1EDKFT01_ResCodeDesc);
					 * 
					 * }
					 */
					else if ("AAR".equals(FTX01)) {
						// Shipping Method
						// IF FTX/4451="AAR" THEN SET
						// E1EDK01-VSART = FTX/C107/4441
						String FTX0301 = (String) iFTX.get("FTX0301");
						e1edk01.setVSART(FTX0301);
					} else if ("OSI".equals(FTX01)) {

						// Entitlement Information
						ZE1EDKFT01 zE1EDKFT01_EntlmtInfo = iDocObjectFactory.createZORD05LENIDOCE1EDK01ZE1EDKFT01();

						String FTX0401 = (String) iFTX.get("FTX0401");
						zE1EDKFT01_EntlmtInfo.setVAL1(FTX0401);

						String FTX0402 = (String) iFTX.get("FTX0402");
						zE1EDKFT01_EntlmtInfo.setVAL2(FTX0402);

						String FTX0403 = (String) iFTX.get("FTX0403");
						zE1EDKFT01_EntlmtInfo.setVAL3(FTX0403);

						String FTX0404 = (String) iFTX.get("FTX0404");
						zE1EDKFT01_EntlmtInfo.setVAL4(FTX0404);

						String FTX0405 = (String) iFTX.get("FTX0405");
						zE1EDKFT01_EntlmtInfo.setVAL5(FTX0405);

						zE1EDKFT01_EntlmtInfo.setQUAL("003");
						

						zE1EDKFT01_EntlmtInfo.setSEGMENT("1");
						e1edk01.getZE1EDKFT01().add(zE1EDKFT01_EntlmtInfo);

					} else if ("DAR".equals(FTX01)) {

						// Diagnostic Information
						ZE1EDKFT01 zE1EDKFT01_DiagnosticInfo = iDocObjectFactory.createZORD05LENIDOCE1EDK01ZE1EDKFT01();

						String FTX0401 = (String) iFTX.get("FTX0401");
						zE1EDKFT01_DiagnosticInfo.setVAL1(FTX0401);

						String FTX0402 = (String) iFTX.get("FTX0402");
						zE1EDKFT01_DiagnosticInfo.setVAL2(FTX0402);

						String FTX0403 = (String) iFTX.get("FTX0403");
						zE1EDKFT01_DiagnosticInfo.setVAL3(FTX0403);

						String FTX0404 = (String) iFTX.get("FTX0404");
						zE1EDKFT01_DiagnosticInfo.setVAL4(FTX0404);

						String FTX0405 = (String) iFTX.get("FTX0405");
						zE1EDKFT01_DiagnosticInfo.setVAL5(FTX0405);

						zE1EDKFT01_DiagnosticInfo.setQUAL("004");
						zE1EDKFT01_DiagnosticInfo.setSEGMENT("1");
						

						e1edk01.getZE1EDKFT01().add(zE1EDKFT01_DiagnosticInfo);

					} else if ("CUR".equals(FTX01)) {

						// Shipping Instructions
						ZE1EDKFT01 zE1EDKFT01_ShippingInstruction = iDocObjectFactory
								.createZORD05LENIDOCE1EDK01ZE1EDKFT01();

						String FTX0401 = (String) iFTX.get("FTX0401");
						zE1EDKFT01_ShippingInstruction.setVAL1(FTX0401);

						String FTX0402 = (String) iFTX.get("FTX0402");
						zE1EDKFT01_ShippingInstruction.setVAL2(FTX0402);

						String FTX0403 = (String) iFTX.get("FTX0403");
						zE1EDKFT01_ShippingInstruction.setVAL3(FTX0403);

						String FTX0404 = (String) iFTX.get("FTX0404");
						zE1EDKFT01_ShippingInstruction.setVAL4(FTX0404);

						String FTX0405 = (String) iFTX.get("FTX0405");
						zE1EDKFT01_ShippingInstruction.setVAL5(FTX0405);

						zE1EDKFT01_ShippingInstruction.setQUAL("001");
						
						zE1EDKFT01_ShippingInstruction.setSEGMENT("1");

						e1edk01.getZE1EDKFT01().add(zE1EDKFT01_ShippingInstruction);

					} else if ("DEL".equals(FTX01)) {
						// Service Order Delivery Type
						// IF FTX/4451="DEL" THEN SET
						// ZE1EDK01-SODLT = FTX/C107/4441
						String FTX0301 = (String) iFTX.get("FTX0301");
						ze1edk01.setSODLT(FTX0301);
					} else if ("PRD".equals(FTX01)) {

						// Product Hierarchy Key
						ZE1EDKFT01 zE1EDKFT01_ProductHierarchyKey = iDocObjectFactory
								.createZORD05LENIDOCE1EDK01ZE1EDKFT01();

						String FTX0401 = (String) iFTX.get("FTX0401");
						zE1EDKFT01_ProductHierarchyKey.setVAL1(FTX0401);

						String FTX0402 = (String) iFTX.get("FTX0402");
						zE1EDKFT01_ProductHierarchyKey.setVAL2(FTX0402);

						String FTX0403 = (String) iFTX.get("FTX0403");
						zE1EDKFT01_ProductHierarchyKey.setVAL3(FTX0403);

						zE1EDKFT01_ProductHierarchyKey.setQUAL("005");
						
						zE1EDKFT01_ProductHierarchyKey.setSEGMENT("1");

						e1edk01.getZE1EDKFT01().add(zE1EDKFT01_ProductHierarchyKey);

					} else if ("SSR".equals(FTX01)) {
						// Service Hours
						// IF FTX/4451="SSR" THEN SET
						// ZEIEDK01-SRVH = FTX/C108/4440_1
						String FTX0401 = (String) iFTX.get("FTX0401");
						ze1edk01.setSRVH(FTX0401);

						// Committed Service Time Window
						// IF FTX/4451="SSR" THEN SET
						// ZEIEDK01-CSTW = FTX/C108/4440_2
						String FTX0402 = (String) iFTX.get("FTX0402");
						ze1edk01.setCSTW(FTX0402);
					} else if ("AAI".equals(FTX01)) {

						// Unit Details
						ZE1EDKFT01 zE1EDKFT01_UnitDetails = iDocObjectFactory.createZORD05LENIDOCE1EDK01ZE1EDKFT01();

						String FTX0401 = (String) iFTX.get("FTX0401");
						zE1EDKFT01_UnitDetails.setVAL1(FTX0401);

						String FTX0402 = (String) iFTX.get("FTX0402");
						zE1EDKFT01_UnitDetails.setVAL2(FTX0402);

						String FTX0403 = (String) iFTX.get("FTX0403");
						zE1EDKFT01_UnitDetails.setVAL3(FTX0403);

						String FTX0404 = (String) iFTX.get("FTX0404");
						zE1EDKFT01_UnitDetails.setVAL4(FTX0404);

						String FTX0405 = (String) iFTX.get("FTX0405");
						zE1EDKFT01_UnitDetails.setVAL5(FTX0405);

						zE1EDKFT01_UnitDetails.setQUAL("006");
						zE1EDKFT01_UnitDetails.setSEGMENT("1");
						e1edk01.getZE1EDKFT01().add(zE1EDKFT01_UnitDetails);

					} else if ("MBD".equals(FTX01)) {

						// IRIS Code Group
						ZE1EDKFT01 zE1EDKFT01_IRISCodeGroup = iDocObjectFactory.createZORD05LENIDOCE1EDK01ZE1EDKFT01();

						String FTX0401 = (String) iFTX.get("FTX0401");
						zE1EDKFT01_IRISCodeGroup.setVAL1(FTX0401);

						String FTX0402 = (String) iFTX.get("FTX0402");
						zE1EDKFT01_IRISCodeGroup.setVAL2(FTX0402);

						String FTX0403 = (String) iFTX.get("FTX0403");
						zE1EDKFT01_IRISCodeGroup.setVAL3(FTX0403);

						String FTX0404 = (String) iFTX.get("FTX0404");
						zE1EDKFT01_IRISCodeGroup.setVAL4(FTX0404);

						String FTX0405 = (String) iFTX.get("FTX0405");
						zE1EDKFT01_IRISCodeGroup.setVAL5(FTX0405);

						zE1EDKFT01_IRISCodeGroup.setQUAL("007");
						zE1EDKFT01_IRISCodeGroup.setSEGMENT("1");
						e1edk01.getZE1EDKFT01().add(zE1EDKFT01_IRISCodeGroup);

					} else if ("RAT".equals(FTX01)) {
						// Special Code
						// IF FTX/4451="RAT" THEN SET
						// ZE1EDK01-SCODE = FTX/C108/D4440_1
						String FTX0401 = (String) iFTX.get("FTX0401");
						ze1edk01.setSCODE(FTX0401);
					} else if ("AAS".equals(FTX01)) {

						// Remark Information
						ZE1EDKFT01 zE1EDKFT01_RemarkInfo = iDocObjectFactory.createZORD05LENIDOCE1EDK01ZE1EDKFT01();

						String FTX0401 = (String) iFTX.get("FTX0401");
						zE1EDKFT01_RemarkInfo.setVAL1(FTX0401);

						String FTX0402 = (String) iFTX.get("FTX0402");
						zE1EDKFT01_RemarkInfo.setVAL2(FTX0402);

						String FTX0403 = (String) iFTX.get("FTX0403");
						zE1EDKFT01_RemarkInfo.setVAL3(FTX0403);

						String FTX0404 = (String) iFTX.get("FTX0404");
						zE1EDKFT01_RemarkInfo.setVAL4(FTX0404);

						String FTX0405 = (String) iFTX.get("FTX0405");
						zE1EDKFT01_RemarkInfo.setVAL5(FTX0405);

						zE1EDKFT01_RemarkInfo.setQUAL("008");
						zE1EDKFT01_RemarkInfo.setSEGMENT("1");
						e1edk01.getZE1EDKFT01().add(zE1EDKFT01_RemarkInfo);

					} else if ("RQT".equals(FTX01)) {

						// Indicator
						ZE1EDKFT01 zE1EDKFT01_Indicator = iDocObjectFactory.createZORD05LENIDOCE1EDK01ZE1EDKFT01();

						String FTX0401 = (String) iFTX.get("FTX0401");
						zE1EDKFT01_Indicator.setVAL1(FTX0401);

						String FTX0402 = (String) iFTX.get("FTX0402");
						zE1EDKFT01_Indicator.setVAL2(FTX0402);

						String FTX0403 = (String) iFTX.get("FTX0403");
						zE1EDKFT01_Indicator.setVAL3(FTX0403);

						String FTX0404 = (String) iFTX.get("FTX0404");
						zE1EDKFT01_Indicator.setVAL4(FTX0404);

						String FTX0405 = (String) iFTX.get("FTX0405");
						zE1EDKFT01_Indicator.setVAL5(FTX0405);

						zE1EDKFT01_Indicator.setQUAL("009");
						zE1EDKFT01_Indicator.setSEGMENT("1");
						e1edk01.getZE1EDKFT01().add(zE1EDKFT01_Indicator);

					} else if ("DIN".equals(FTX01)) {

						// Service Delivery Instruction
						ZE1EDKFT01 zE1EDKFT01_ServiceDelvInstr = iDocObjectFactory
								.createZORD05LENIDOCE1EDK01ZE1EDKFT01();

						String FTX0401 = (String) iFTX.get("FTX0401");
						zE1EDKFT01_ServiceDelvInstr.setVAL1(FTX0401);

						String FTX0402 = (String) iFTX.get("FTX0402");
						zE1EDKFT01_ServiceDelvInstr.setVAL2(FTX0402);

						String FTX0403 = (String) iFTX.get("FTX0403");
						zE1EDKFT01_ServiceDelvInstr.setVAL3(FTX0403);

						String FTX0404 = (String) iFTX.get("FTX0404");
						zE1EDKFT01_ServiceDelvInstr.setVAL4(FTX0404);

						String FTX0405 = (String) iFTX.get("FTX0405");
						zE1EDKFT01_ServiceDelvInstr.setVAL5(FTX0405);

						zE1EDKFT01_ServiceDelvInstr.setQUAL("002");
						zE1EDKFT01_ServiceDelvInstr.setSEGMENT("1");
						e1edk01.getZE1EDKFT01().add(zE1EDKFT01_ServiceDelvInstr);
					}
				}
			}

			// Group 1 - RFF and DTM
			List Group1 = (List) heading.get("0080_Segment_group_1");
			if (Group1 != null) {
				for (int iGrp1 = 0; iGrp1 < Group1.size(); iGrp1++) {

					HashMap group1 = (HashMap) Group1.get(iGrp1);

					// RFF
					HashMap RFF = (HashMap) group1.get("0090_RFF");
					String RFF0101 = (String) RFF.get("RFF0101");
					E1EDK02 e1EDK02 = iDocObjectFactory.createZORD05LENIDOCE1EDK02();
					e1EDK02.setSEGMENT("1");
					if ("ACW".equals(RFF0101)) {
						// Service Order Reference Number
						// IF G1/RFF/C506/1153="ACW" THEN SET
						// ZE1EDK01-SORN = G1/RFF/C506/1154
						String RFF0102 = (String) RFF.get("RFF0102");
						ze1edk01.setSORN(RFF0102);
					} else if ("CO".equals(RFF0101)) {
						// Service Order Number
						// IF G1/RFF/C506/1153="CO" THEN SET
						// E1EDK02-BELNR = G1/RFF/C506/1154 AND E1EDK02-QUAL =
						// "001"
						String RFF0102 = (String) RFF.get("RFF0102");
						
						e1EDK02.setBELNR(RFF0102);
						e1EDK02.setQUALF("001");

						orderResponseIDoc.getIDOC().getE1EDK02().add(e1EDK02);
					} else if ("PC".equals(RFF0101)) {
						// Machine Product ID
						// IF G1/RFF/C506/1153="PC" THEN
						// SET E1EDK01-ABRVW_BEZ = G1/RFF/C506/1154
						String RFF0102 = (String) RFF.get("RFF0102");
						e1edk01.setABRVWBEZ(RFF0102);
					} else if ("SE".equals(RFF0101)) {
						// Machine Serial Number
						// IF G1/RFF/C506/1153="SE" THEN
						// SET E1EDK01-AUGRU_BEZ = G1/RFF/C506/1154
						String RFF0102 = (String) RFF.get("RFF0102");
						e1edk01.setAUGRUBEZ(RFF0102);
					} else if ("SZ".equals(RFF0101)) {
						// Service Level
						// IF G1/RFF/C506/1153="SZ" THEN
						// SET E1EDK01-ZTERM = G1/RFF/C506/1154
						String RFF0102 = (String) RFF.get("RFF0102");
						e1edk01.setZTERM(RFF0102);
					} else if ("ABO".equals(RFF0101)) {
						// Hard Drive Retenion Indicator
						// IF G1/RFF/C506/1153="ABO" THEN
						// SET E1EDK01-ABRVW = G1/RFF/C506/1154
						String RFF0102 = (String) RFF.get("RFF0102");
						e1edk01.setABRVW(RFF0102);
					} else if ("ADG".equals(RFF0101)) {
						// CID Coverage
						// IF G1/RFF/C506/1153="ADG" THEN
						// SET E1EDK01-ABTNR = G1/RFF/C506/1154
						String RFF0102 = (String) RFF.get("RFF0102");
						e1edk01.setABTNR(RFF0102);
					} else if ("ACH".equals(RFF0101)) {
						// CID Case Indicator
						// IF G1/RFF/C506/1153="ACH" THEN SET
						// E1EDK01-DELCO = G1/RFF/C506/1154
						String RFF0102 = (String) RFF.get("RFF0102");
						e1edk01.setDELCO(RFF0102);
					}

					// Group DTM
					List DTMList = (List) group1.get("0100_DTM");
					if (DTMList != null) {
						for (int iDtm = 0; iDtm < DTMList.size(); iDtm++) {
							HashMap GDTM = (HashMap) DTMList.get(iDtm);

							String DTM0101 = (String) GDTM.get("DTM0101");
							String DTM0102 = (String) GDTM.get("DTM0102");
							String DTM0103 = (String) GDTM.get("DTM0103");
							E1EDK03 e1EDK03 = iDocObjectFactory.createZORD05LENIDOCE1EDK03();
							e1EDK03.setSEGMENT("1");
							if ("4".equals(DTM0101) && "304".equals(DTM0103)) {
								// Service Order Number DTM
								// E1EDK03-DATUM+UZEIT = G1/DTM/C507/2380 AND
								// E1EDK03-QUAL = "022"
								
								e1EDK03.setDATUM(DTM0102);
								e1EDK03.setUZEIT(DTM0102.substring(8));
								e1EDK03.setIDDAT("022");
								orderResponseIDoc.getIDOC().getE1EDK03().add(e1EDK03);
							} else if ("171".equals(DTM0101)) {
								// Service Order Reference DTM
								// E1EDK03-DATUM+UZEIT = G1/DTM/C507/2380 AND
								// E1EDK03-QUAL = "014"
								//E1EDK03 e1EDK03 = iDocObjectFactory.createZORD05LENIDOCE1EDK03();
								e1EDK03.setDATUM(DTM0102);
								e1EDK03.setUZEIT(DTM0102.substring(8));
								e1EDK03.setIDDAT("014");
								orderResponseIDoc.getIDOC().getE1EDK03().add(e1EDK03);
							} else if ("201".equals(DTM0101)) {
								// Customer Request Pick Up DTM
								// E1EDK03-DATUM+UZEIT = G1/DTM/C507/2380 AND
								// E1EDK03-QUAL = "002"
								//E1EDK03 e1EDK03 = iDocObjectFactory.createZORD05LENIDOCE1EDK03();
								e1EDK03.setDATUM(DTM0102);
								e1EDK03.setUZEIT(DTM0102.substring(8));
								e1EDK03.setIDDAT("002");
								orderResponseIDoc.getIDOC().getE1EDK03().add(e1EDK03);
							}
						}
					}
				}
			}

			// Group 2 - NAD etc.
			List Group2 = (List) heading.get("0110_Segment_group_2");

			if (Group2 != null) {
				E1EDKA1 e1EDKA1Buyer = iDocObjectFactory.createZORD05LENIDOCE1EDKA1();
				e1EDKA1Buyer.setSEGMENT("1");
				e1EDKA1Buyer.setPARVW("BY");

				E1EDKA1 e1EDKA1Customer = iDocObjectFactory.createZORD05LENIDOCE1EDKA1();
				e1EDKA1Customer.setSEGMENT("1");
				e1EDKA1Customer.setPARVW("OY");

				for (int iGrp3 = 0; iGrp3 < Group2.size(); iGrp3++) {

					HashMap group3 = (HashMap) Group2.get(iGrp3);

					// NAD
					HashMap NAD = (HashMap) group3.get("0120_NAD");
					if (NAD != null && !NAD.isEmpty()) {

						String NAD01 = (String) NAD.get("NAD01");
						if ("SE".equals(NAD01)) {

							E1EDKA1 e1EDKA1 = iDocObjectFactory.createZORD05LENIDOCE1EDKA1();
							e1EDKA1.setSEGMENT("1");

							// Seller BP ID
							// E1EDKA1-PARTN = G3/NAD/C082/3039
							String NAD0201 = (String) NAD.get("NAD0201");

							// Seller Name
							// E1EDKA1-NAME1 = G3/NAD/C080/3036_1
							String NAD0401 = (String) NAD.get("NAD0401");

							e1EDKA1.setPARTN(NAD0201);
							e1EDKA1.setNAME1(NAD0401);

							e1EDKA1.setPARVW("SE");

							orderResponseIDoc.getIDOC().getE1EDKA1().add(e1EDKA1);

						} else if ("BY".equals(NAD01)) {

							// Buyer BP ID
							// E1EDKA1-PARTN = G3/NAD/C082/3039
							String NAD0201 = (String) NAD.get("NAD0201");
							e1EDKA1Buyer.setPARTN(NAD0201);

							// Buyer Name
							// E1EDKA1-NAME1 = G3/NAD/C080/3036_1
							String NAD0401 = (String) NAD.get("NAD0401");
							e1EDKA1Buyer.setNAME1(NAD0401);

							// Buyer State
							// E1EDKA1-ORT02 = G3/NAD/C080/3036_2
							String NAD0402 = (String) NAD.get("NAD0402");
							e1EDKA1Buyer.setORT02(NAD0402);

							// Buyer Address
							// E1EDKA1-NAME2 = G3/NAD/C059/3042_1
							// E1EDKA1-NAME3 = G3/NAD/C059/3042_2
							// E1EDKA1-NAME4 = G3/NAD/C059/3042_3
							// E1EDKA1-STRAS = G3/NAD/C059/3042_4
							String NAD0501 = (String) NAD.get("NAD0501");
							String NAD0502 = (String) NAD.get("NAD0502");
							String NAD0503 = (String) NAD.get("NAD0503");
							String NAD0504 = (String) NAD.get("NAD0504");
							e1EDKA1Buyer.setNAME2(NAD0501);
							e1EDKA1Buyer.setNAME3(NAD0502);
							e1EDKA1Buyer.setNAME4(NAD0503);
							e1EDKA1Buyer.setSTRAS(NAD0504);

							// Buyer City
							// E1EDKA1-ORT01 = G3/NAD/3164
							String NAD06 = (String) NAD.get("NAD06");
							e1EDKA1Buyer.setORT01(NAD06);

							// Buyer Post Code
							// E1EDKA1-PSTLZ = G3/NAD/3251
							String NAD08 = (String) NAD.get("NAD08");
							e1EDKA1Buyer.setPSTLZ(NAD08);

							// Buyer Country
							// E1EDKA1-LAND1 = G3/NAD/3207
							String NAD09 = (String) NAD.get("NAD09");
							e1EDKA1Buyer.setLAND1(NAD09);

							orderResponseIDoc.getIDOC().getE1EDKA1().add(e1EDKA1Buyer);

						} else if ("OY".equals(NAD01)) {

							// Customer BP ID
							// E1EDKA1-PARTN = G3/NAD/C082/3039
							String NAD0201 = (String) NAD.get("NAD0201");
							e1EDKA1Customer.setPARTN(NAD0201);

							// Customer Name (Company name)
							// E1EDKA1-NAME1 = G3/NAD/C080/3036_1
							String NAD0401 = (String) NAD.get("NAD0401");
							e1EDKA1Customer.setNAME1(NAD0401);

							// Customer State
							// E1EDKA1-NAME1 = G3/NAD/C080/3036_2
							String NAD0402 = (String) NAD.get("NAD0402");
							e1EDKA1Customer.setORT02(NAD0402);

							// Customer Address
							// E1EDKA1-NAME2 = G3/NAD/C059/3042_1
							// E1EDKA1-NAME3 = G3/NAD/C059/3042_2
							// E1EDKA1-NAME4 = G3/NAD/C059/3042_3
							// E1EDKA1-STRAS = G3/NAD/C059/3042_4
							String NAD0501 = (String) NAD.get("NAD0501");
							String NAD0502 = (String) NAD.get("NAD0502");
							String NAD0503 = (String) NAD.get("NAD0503");
							String NAD0504 = (String) NAD.get("NAD0504");
							e1EDKA1Customer.setNAME2(NAD0501);
							e1EDKA1Customer.setNAME3(NAD0502);
							e1EDKA1Customer.setNAME4(NAD0503);
							e1EDKA1Customer.setSTRAS(NAD0504);

							// Customer City
							// E1EDKA1-ORT01 = G3/NAD/3164
							String NAD06 = (String) NAD.get("NAD06");
							e1EDKA1Customer.setORT01(NAD06);

							// Customer Post Code
							// E1EDKA1-PSTLZ = G3/NAD/3251
							String NAD08 = (String) NAD.get("NAD08");
							e1EDKA1Customer.setPSTLZ(NAD08);

							// Customer Country
							// E1EDKA1-LAND1 = G3/NAD/3207
							String NAD09 = (String) NAD.get("NAD09");
							e1EDKA1Customer.setLAND1(NAD09);

							orderResponseIDoc.getIDOC().getE1EDKA1().add(e1EDKA1Customer);
						}

						// Group 5
						List Group6 = (List) group3.get("0210_Segment_group_5");
						if (Group6 != null) {
							for (int iGrp6 = 0; iGrp6 < Group6.size(); iGrp6++) {

								HashMap group6 = (HashMap) Group6.get(iGrp6);
								// CTA
								HashMap CTA = (HashMap) group6.get("0220_CTA");
								String CTA01 = (String) CTA.get("CTA01");
								if ("OY".equals(NAD01) && "PD".equals(CTA01)) {
									// Buyer Contact Person Name
									// E1EDKA1-STRS2 = G6/CTA/C056/3412 AND
									// E1EDKA1-QUAL = "BY"
									String CTA0202 = (String) CTA.get("CTA0202");
									e1EDKA1Customer.setSTRS2(CTA0202);

								} else if ("BY".equals(NAD01) && "CR".equals(CTA01)) {
									// Customer Contact Person Name
									// E1EDKA1-STRS2 = G6/CTA/C056/3412 AND
									// E1EDKA1-QUAL = "BY"
									String CTA0202 = (String) CTA.get("CTA0202");
									e1EDKA1Buyer.setSTRS2(CTA0202);
								}

								// COM
								List COMList = (List) group6.get("0230_COM");
								if (COMList != null) {
									for (int iCOM = 0; iCOM < COMList.size(); iCOM++) {
										HashMap COM = (HashMap) COMList.get(iCOM);
										String COM0102 = (String) COM.get("COM0102");
										if ("EM".equals(COM0102)) {
											// Buyer Email
											// SET E1EDKA1-ILNNR =
											// G6/COM/C076/3148
											// AND E1EDKA1-QUAL = "BY"
											String COM0101 = (String) COM.get("COM0101");
											if ("OY".equals(NAD01)) {
												e1EDKA1Customer.setILNNR(COM0101);
											} else if ("BY".equals(NAD01)) {
												e1EDKA1Buyer.setILNNR(COM0101);
											}
										} else if ("TE".equals(COM0102)) {
											// Buyer Telephone
											// SET E1EDKA1-TELF1 =
											// G6/COM/C076/3148
											// AND E1EDKA1-QUAL = "BY"
											String COM0101 = (String) COM.get("COM0101");
											if ("OY".equals(NAD01)) {
												e1EDKA1Customer.setTELF1(COM0101);
											} else if ("BY".equals(NAD01)) {
												e1EDKA1Buyer.setTELF1(COM0101);
											}
										}
									}
								}

							}
						}
					}
				}
			}

			// Group 28 - LIN etc.
			List Group26 = (List) detail.get("1010_Segment_group_28");
			if (Group26 != null) {
				for (int iGrp26 = 0; iGrp26 < Group26.size(); iGrp26++) {

					HashMap group26 = (HashMap) Group26.get(iGrp26);
					E1EDP01 e1EDP01 = iDocObjectFactory.createZORD05LENIDOCE1EDP01();
					e1EDP01.setSEGMENT("1");
					
					E1EDP19 e1EDP01E1EDP19 = iDocObjectFactory.createZORD05LENIDOCE1EDP01E1EDP19();
					e1EDP01E1EDP19.setSEGMENT("1");
					
					e1EDP01.getE1EDP19().add(e1EDP01E1EDP19);
					ZE1EDP01 zE1EDP01 = iDocObjectFactory.createZORD05LENIDOCE1EDP01ZE1EDP01();
					zE1EDP01.setSEGMENT("1");
					
					

					orderResponseIDoc.getIDOC().getE1EDP01().add(e1EDP01);

					// LIN
					HashMap LIN = (HashMap) group26.get("1020_LIN");

					// Line Item ID
					// SET E1EDP01-POSEX = G26/LIN/1082
					if (LIN != null && !LIN.isEmpty()) {
						String LIN01 = (String) LIN.get("LIN01");
						e1EDP01.setPOSEX(LIN01);

						// Reference Item ID
						// IF G26/LIN/C829/5495="1" THEN SET E1EDP01-ABFTZ =
						// G26/LIN/C829/1082
						String LIN0401 = (String) LIN.get("LIN0401");
						if ("1".equals(LIN0401)) {
							String LIN0402 = (String) LIN.get("LIN0402");
							e1EDP01.setABFTZ(LIN0402);
						}
					}
					// Line Material ID
					// IF G26/PIA/4347="5" THEN SET E1EDP19-IDTNR =
					// G26/PIA/C212_1/7140
					// PIA
					List PIAList = (List) group26.get("1030_PIA");
					if (PIAList != null) {
						for (int iPia = 0; iPia < PIAList.size(); iPia++) {
							HashMap PIA = (HashMap) PIAList.get(iPia);
							String PIA01 = (String) PIA.get("PIA01");
							if ("5".equals(PIA01)) {
								String PIA0301 = (String) PIA.get("PIA0201");
								e1EDP01E1EDP19.setIDTNR(PIA0301);
								e1EDP01E1EDP19.setQUALF("002");
							}
						}
					}

					// Line Description
					// IF G26/IMD/7077="F" THEN SET
					// E1EDP19-KTEXT = Concatenation of G26/IMD/C273/7008_1 and
					// G26/IMD/C273/7008_2

					// IMD
					List IMDList = (List) group26.get("1040_IMD");
					if (IMDList != null) {
						for (int iImd = 0; iImd < IMDList.size(); iImd++) {
							HashMap IMD = (HashMap) IMDList.get(iImd);
							String IMD01 = (String) IMD.get("IMD01");
							if ("F".equals(IMD01)) {
								String IMD0304 = (String) IMD.get("IMD0304");
								String IMD0305 = (String) IMD.get("IMD0305");
								String kText = null;
								if (IMD0304 != null) {
									if (IMD0305 != null) {
										kText = IMD0304 + " " + IMD0305;
									} else {
										kText = IMD0304;
									}
								} else {
									if (IMD0305 != null) {
										kText = IMD0305;
									}
								}

								e1EDP01E1EDP19.setKTEXT(kText);

							}
						}
					}

					// Line Item Quantity Measurement
					// IF G26/QTY/C186/6063="21" THEN SET E1EDP01-MENEE =
					// G26/QTY/C186/6411
					// Line Item Quantity
					// IF G26/QTY/C186/6063="21" THEN SET E1EDP01-MENGE =
					// G26/QTY/C186/6060
					List QTYList = (List) group26.get("1060_QTY");
					if (QTYList != null) {
						for (int iQty = 0; iQty < QTYList.size(); iQty++) {
							HashMap QTY = (HashMap) QTYList.get(iQty);
							String QTY0101 = (String) QTY.get("QTY0101");
							if ("21".equals(QTY0101)) {
								String QTY0103 = (String) QTY.get("QTY0103");
								//Integer QTY0102 = (Integer)QTY.get("QTY0102");
								e1EDP01.setMENEE(QTY0103);
								e1EDP01.setMENGE( QTY.get("QTY0102").toString());
							}
						}
					}

					// Commited Service Date
					// If G26/DTM/C507/2005(qualifier) = "69" and
					// G26/DTM/C507/2379 = "304"
					// Then extract "20161114150000" from "20161114150000UTC?+2"
					// and assign it to ZE1EDK01-CSD

					// DTM
					List LDTM = (List) group26.get("1090_DTM");
					if (LDTM != null) {
						for (int idtm = 0; idtm < LDTM.size(); idtm++) {

							HashMap iDTM = (HashMap) LDTM.get(idtm);
							String DTM0101 = (String) iDTM.get("DTM0101");
							String DTM0102 = (String) iDTM.get("DTM0102");
							String DTM0103 = (String) iDTM.get("DTM0103");
							if ("69".equals(DTM0101) && "304".equals(DTM0103)) {
								// Then set ZE1EDP01 to CSD
								if (DTM0102 != null) {
									int indexOfUTC = DTM0102.indexOf("UTC");
									String CSD = DTM0102;
									if (indexOfUTC != -1) {
										CSD = DTM0102.substring(0, indexOfUTC);
										CSD = CSD.substring(0,CSD.length());
									}
									zE1EDP01.setCSD(CSD);
								}
							}
							else{
								zE1EDP01.setCSD("-");
							}
							e1EDP01.setZE1EDP01(zE1EDP01);
						}
					}

					List LFTX = (List) group26.get("1160_FTX");

					if (LFTX != null) {

						for (int iftx = 0; iftx < LFTX.size(); iftx++) {

							HashMap iFTX = (HashMap) LFTX.get(iftx);
							String FTX01 = (String) iFTX.get("FTX01");
							 if ("OSI".equals(FTX01)) { //IF  G28/FTX/4451 = "OSI" THEN SET E1EDP01/PSTYP = "G28/FTX/C108/4440_1"<PSTYP>X</PSTYP>
								// Return Indicator
								// IF G26/FTX/4451="OSI" THEN SET
								// E1EDP01-PSTYP = G26/FTX/C108/4440_1
								String FTX0401 = (String) iFTX.get("FTX0401");
								e1EDP01.setPSTYP(FTX0401);
							} else if ("LIN".equals(FTX01)) {
								// Item Category
								// IF G26/FTX/4451="LIN" THEN SET
								// E1EDP01-PSTYV = G26/FTX/C108/4440_1
								String FTX0401 = (String) iFTX.get("FTX0401");
								e1EDP01.setPSTYV(FTX0401);
							}else if ("AAG".equals(FTX01)) {
								// Item Category
								// IF G26/FTX/4451="LIN" THEN SET
								// E1EDP01-PSTYV = G26/FTX/C108/4440_1
								String FTX0401 = (String) iFTX.get("FTX0401");
								e1EDP01.setUEPOS(FTX0401);
							}
							else if ("AEA".equals(FTX01)) {

								// Remark Information
								// IF G26/FTX/4451="AEA" THEN SET
								// ZE1EDPFT01-VAL1 = G26/FTX/C108/4440_1
								// ZE1EDPFT01-VAL2 = G26/FTX/C108/4440_2
								// ZE1EDPFT01-VAL3 = G26/FTX/C108/4440_3
								// ZE1EDPFT01-VAL4 = G26/FTX/C108/4440_4
								// ZE1EDPFT01-VAL5 = G26/FTX/C108/4440_5

								ZE1EDPFT01 zE1EDPFT01_RemarkInfo = iDocObjectFactory
										.createZORD05LENIDOCE1EDP01ZE1EDPFT01();

								// AND ZE1EDPFT01-QUAL = "008"
								String FTX0401 = (String) iFTX.get("FTX0401");
								zE1EDPFT01_RemarkInfo.setVAL1(FTX0401);

								String FTX0402 = (String) iFTX.get("FTX0402");
								zE1EDPFT01_RemarkInfo.setVAL2(FTX0402);

								String FTX0403 = (String) iFTX.get("FTX0403");
								zE1EDPFT01_RemarkInfo.setVAL3(FTX0403);

								String FTX0404 = (String) iFTX.get("FTX0404");
								zE1EDPFT01_RemarkInfo.setVAL4(FTX0404);

								String FTX0405 = (String) iFTX.get("FTX0405");
								zE1EDPFT01_RemarkInfo.setVAL5(FTX0405);

								zE1EDPFT01_RemarkInfo.setQUAL("008");

								e1EDP01.getZE1EDPFT01().add(zE1EDPFT01_RemarkInfo);

							} else if ("IDC".equals(FTX01)) {

								// Indicator
								ZE1EDPFT01 zE1EDPFT01_Indicator = iDocObjectFactory
										.createZORD05LENIDOCE1EDP01ZE1EDPFT01();

								// IF G26/FTX/4451="IDC" THEN SET
								// ZE1EDPFT01-VAL1= G26/FTX/C108/4440_1
								// ZE1EDPFT01-VAL2= G26/FTX/C108/4440_2
								// ZE1EDPFT01-VAL3= G26/FTX/C108/4440_3
								// ZE1EDPFT01-VAL4= G26/FTX/C108/4440_4
								// ZE1EDPFT01-VAL5 = G26/FTX/C108/4440_
								// AND ZE1EDPFT01-QUAL = "009"

								String FTX0401 = (String) iFTX.get("FTX0401");
								zE1EDPFT01_Indicator.setVAL1(FTX0401);

								String FTX0402 = (String) iFTX.get("FTX0402");
								zE1EDPFT01_Indicator.setVAL2(FTX0402);

								String FTX0403 = (String) iFTX.get("FTX0403");
								zE1EDPFT01_Indicator.setVAL3(FTX0403);

								String FTX0404 = (String) iFTX.get("FTX0404");
								zE1EDPFT01_Indicator.setVAL4(FTX0404);

								String FTX0405 = (String) iFTX.get("FTX0405");
								zE1EDPFT01_Indicator.setVAL5(FTX0405);

								zE1EDPFT01_Indicator.setQUAL("009");

								e1EDP01.getZE1EDPFT01().add(zE1EDPFT01_Indicator);

							}
						}
					}

					// Group 33 - RFF, DTM
					List Group31 = (List) group26.get("1340_Segment_group_33");
					if (Group31 != null) {
						for (int iGrp31 = 0; iGrp31 < Group31.size(); iGrp31++) {

							HashMap group31 = (HashMap) Group31.get(iGrp31);

							// Parts Serial Number
							// IF G31/RFF/C506/1153="SE" THEN
							// SET E1EDP01-MATNR = G31/RFF/C506/1154
							// RFF
							HashMap RFF = (HashMap) group31.get("1350_RFF");
							String RFF0101 = (String) RFF.get("RFF0101");
							if ("SE".equals(RFF0101)) {
								String RFF0102 = (String) RFF.get("RFF0102");
								e1EDP01.setMATNR(RFF0102);
							}
						}
					}

					// START
					// Group 39 - NAD etc.
					List Group37 = (List) group26.get("1590_Segment_group_39"); // GRP
																				// 39
					if (Group37 != null) {

						E1EDPA1 e1EDPA1Customer = iDocObjectFactory.createZORD05LENIDOCE1EDP01E1EDPA1();
						e1EDPA1Customer.setSEGMENT("1");
						e1EDPA1Customer.setPARVW("OY");

						for (int iGrp37 = 0; iGrp37 < Group37.size(); iGrp37++) {

							HashMap group37 = (HashMap) Group37.get(iGrp37);

							// NAD
							HashMap NAD = (HashMap) group37.get("1600_NAD");
							if (NAD != null && !NAD.isEmpty()) {

								String NAD01 = (String) NAD.get("NAD01");
								if ("OY".equals(NAD01)) {

									// Customer BP ID
									// E1EDPA1-PARTN = G37/NAD/C082/3039
									String NAD0201 = (String) NAD.get("NAD0201");
									e1EDPA1Customer.setPARTN(NAD0201);

									// Customer Name (Company name)
									// E1EDPA1-NAME1 = G37/NAD/C080/3036_1
									String NAD0401 = (String) NAD.get("NAD0401");
									e1EDPA1Customer.setNAME1(NAD0401);

									// Customer State
									// E1EDPA1-ORT02 = G37/NAD/C080/3036_2
									String NAD0402 = (String) NAD.get("NAD0402");
									e1EDPA1Customer.setORT02(NAD0402);

									// Customer Address
									// E1EDPA1-NAME2 = G37/NAD/C059/3042_1
									// E1EDPA1-NAME3 = G37/NAD/C059/3042_2
									// E1EDPA1-NAME4 = G37/NAD/C059/3042_3
									// E1EDPA1-STRAS = G37/NAD/C059/3042_4
									String NAD0501 = (String) NAD.get("NAD0501");
									String NAD0502 = (String) NAD.get("NAD0502");
									String NAD0503 = (String) NAD.get("NAD0503");
									String NAD0504 = (String) NAD.get("NAD0504");
									e1EDPA1Customer.setNAME2(NAD0501);
									e1EDPA1Customer.setNAME3(NAD0502);
									e1EDPA1Customer.setNAME4(NAD0503);
									e1EDPA1Customer.setSTRAS(NAD0504);

									// Customer City
									// E1EDPA1-ORT01 = G37/NAD/3164
									String NAD06 = (String) NAD.get("NAD06");
									e1EDPA1Customer.setORT01(NAD06);

									// Customer Post Code
									// E1EDPA1-PSTLZ = G37/NAD/3251
									String NAD08 = (String) NAD.get("NAD08");
									e1EDPA1Customer.setPSTLZ(NAD08);

									// Customer Country
									// E1EDPA1-LAND1 = G37/NAD/3207
									String NAD09 = (String) NAD.get("NAD09");
									e1EDPA1Customer.setLAND1(NAD09);

									e1EDP01.getE1EDPA1().add(e1EDPA1Customer);

								}

								// Group 42
								List Group40 = (List) group37.get("1680_Segment_group_42");
								if (Group40 != null) {
									for (int iGrp40 = 0; iGrp40 < Group40.size(); iGrp40++) {

										HashMap group40 = (HashMap) Group40.get(iGrp40);
										// CTA
										HashMap CTA = (HashMap) group40.get("1690_CTA");
										String CTA01 = (String) CTA.get("CTA01");
										if ("OY".equals(NAD01) && "IC".equals(CTA01)) {
											// Customer Contact Person Name
											// E1EDPA1-STRS2 = G40/CTA/C506/3412
											String CTA0202 = (String) CTA.get("CTA0202");
											e1EDPA1Customer.setSTRS2(CTA0202);
										}

										// COM
										List COMList40 = (List) group40.get("1700_COM");
										if (COMList40 != null) {
											for (int iCOM = 0; iCOM < COMList40.size(); iCOM++) {
												HashMap COM = (HashMap) COMList40.get(iCOM);
												String COM0102 = (String) COM.get("COM0102");
												if ("EM".equals(COM0102)) {
													// Customer Email
													// E1EDPA1-ILNNR =
													// G40/COM/C076/3148
													// AND E1EDPA1-QUAL = "OY"
													String COM0101 = (String) COM.get("COM0101");

													if ("OY".equals(NAD01)) {
														e1EDPA1Customer.setILNNR(COM0101);
													}
												} else if ("TE".equals(COM0102)) {
													// Customer Telephone
													// E1EDPA1-TELF1 =
													// G40/COM/C076/3148
													// AND E1EDPA1-QUAL = "OY"
													String COM0101 = (String) COM.get("COM0101");
													if ("OY".equals(NAD01)) {
														e1EDPA1Customer.setTELF1(COM0101);
													}
												}
											}
										}
									}
								}
							}
						}
					}
					// END

				}
			}
		}

		return outputIDocs;
	}

	private static ZORD05LEN createZORD05LEN(ObjectFactory iDocObjectFactory) {

		// Create ZORD05LEN and set IDoc
		ZORD05LEN orderResponseIDoc = iDocObjectFactory.createZORD05LEN();
		IDOC IDoc = iDocObjectFactory.createZORD05LENIDOC();
		orderResponseIDoc.setIDOC(IDoc);

		// Set
		EDIDC40 eDIDC40 = iDocObjectFactory.createZORD05LENIDOCEDIDC40();
		IDoc.setEDIDC40(eDIDC40);
		// Set E1EDK01 segment
		E1EDK01 e1EDK01 = iDocObjectFactory.createZORD05LENIDOCE1EDK01();
		IDoc.setE1EDK01(e1EDK01);
		// Set ZE1EDK01 segment
		ZE1EDK01 zE1EDK01 = iDocObjectFactory.createZORD05LENIDOCE1EDK01ZE1EDK01();
		e1EDK01.setZE1EDK01(zE1EDK01);
		

		return orderResponseIDoc;

	}
	public ConnectEnterpriseMessage getEnterpriseMsg(String str) throws IOException {

		ConnectEnterpriseMessage msg = new ConnectEnterpriseMessage();
		EnterpriseBody value = new EnterpriseBody();
		EnterpriseHeader header = new EnterpriseHeader();
		// String line = null;
		// Set edifact xml to EnterpriseMessage body
		JAXBElement<Object> jaxbElement = new JAXBElement(new QName("payload"), Object.class, str);

		value.setAny(jaxbElement);
		header.setMessageId("123456");
		header.setAction("inbound");
		header.setServiceName("i3klen011");
		header.setMessageSource("lenovo");
		msg.setEnterpriseBody(value);
		msg.setEnterpriseHeader(header);
		return msg;
	}

}
