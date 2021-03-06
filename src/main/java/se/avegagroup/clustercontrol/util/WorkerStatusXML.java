package se.avegagroup.clustercontrol.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import se.avegagroup.clustercontrol.configuration.Constants;
import se.avegagroup.clustercontrol.domain.JkStatus;

public class WorkerStatusXML extends IWorkerStatus {
	
	private static final Logger logger = LoggerFactory.getLogger(WorkerStatusXML.class);
	
	public WorkerStatusXML(String body) {
		getJkStatus(body);
	}
	
	/**
	 * 
	 * @param body
	 * @return
	 */
	private void getJkStatus(String body) {
		Schema mySchema;
		SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
	
		try {
			URL url = getClass().getResource("/xsd/jkStatus.xsd");
		    File schemaFile = new File(url.toURI());
			mySchema = sf.newSchema( schemaFile );
		} catch( SAXException saxe ){
		    // xsd file not correct
		    mySchema = null;
		} catch (URISyntaxException e) {
		    // no schema found at supplied url
			mySchema = null;
		}
		try {
			JAXBContext jc = JAXBContext.newInstance(Constants.JAXB_DOMAIN_NAMESPACE);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(mySchema);
			unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			
			//
			// convert string to byte sequence and create the input stream
			byte currentXMLBytes[] = body.getBytes();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes); 
			@SuppressWarnings("unchecked")
			JAXBElement<JkStatus> status = (JAXBElement<JkStatus>) unmarshaller.unmarshal(byteArrayInputStream);
			this.jkStatus = status.getValue();
		} catch (JAXBException e) {
			logger.error("Could not unmarshal file: "+e.getErrorCode()+": "+e.getMessage());
		}
	}
}