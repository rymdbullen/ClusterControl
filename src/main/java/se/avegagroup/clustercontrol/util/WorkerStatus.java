package se.avegagroup.clustercontrol.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import se.avegagroup.clustercontrol.data.JkStatusType;

public class WorkerStatus {
	
	private static final Log logger = LogFactory.getLog(WorkerStatus.class);
	//private static final Logger logger = LoggerFactory.getLogger(WorkerStatus.class);
	
	/**
	 * 
	 * @throws JAXBException
	 * @throws FileNotFoundException 
	 * @throws URISyntaxException 
	 */
	@SuppressWarnings("unchecked")
	public JAXBElement<JkStatusType> unmarshall(String body) {
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
			JAXBContext jc = JAXBContext.newInstance("se.avegagroup.clustercontrol.data");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(mySchema);
			unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			
			//
			// convert string to byte sequence and create the input stream
			byte currentXMLBytes[] = body.getBytes();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes); 
			
			JAXBElement<JkStatusType> status = (JAXBElement<JkStatusType>) unmarshaller.unmarshal(byteArrayInputStream);
			return status;
		} catch (JAXBException e) {
			logger.error("Could not unmarshall file: "+e.getErrorCode()+": "+e.getMessage());
			return null;
		}
	}
}