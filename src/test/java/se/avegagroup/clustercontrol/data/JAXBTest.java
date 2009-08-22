package se.avegagroup.clustercontrol.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class JAXBTest extends TestCase {
	/**
	 * 
	 * @throws JAXBException
	 * @throws FileNotFoundException 
	 * @throws URISyntaxException 
	 */
	@SuppressWarnings("unchecked")
	public void testUnmarshallStatus() throws JAXBException, FileNotFoundException, URISyntaxException {
		JAXBContext jc = JAXBContext.newInstance("se.avegagroup.clustercontrol.data");
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		
		Schema mySchema;
		SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		try {
			URL url = getClass().getResource("/xsd/jkStatus.xsd");
		    File schemaFile = new File(url.toURI());
			mySchema = sf.newSchema( schemaFile );
		} catch( SAXException saxe ){
		    // ...(error handling)
		    mySchema = null;
		}
		
		unmarshaller.setSchema(mySchema);
		unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		//
		// Open file
		final FileInputStream fis = new FileInputStream("src/test/resources/status.xml");

		JAXBElement<JkStatusType> jkStatusType = (JAXBElement<JkStatusType>) unmarshaller.unmarshal(fis);
		JkStatusType status = jkStatusType.getValue();
		System.out.println("Balancers.getCount()="+status.getBalancers().getCount());
	}
	/**
	 * 
	 * @throws JAXBException
	 * @throws FileNotFoundException 
	 * @throws URISyntaxException 
	 */
	@SuppressWarnings("unchecked")
	public void testUnmarshallActionStatus() throws JAXBException, FileNotFoundException, URISyntaxException {
		JAXBContext jc = JAXBContext.newInstance("se.avegagroup.clustercontrol.data");
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		
		Schema mySchema;
		SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		try {
			URL url = getClass().getResource("/xsd/jkStatus.xsd");
		    File schemaFile = new File(url.toURI());
			mySchema = sf.newSchema( schemaFile );
		} catch( SAXException saxe ){
		    // ...(error handling)
		    mySchema = null;
		}
		
		unmarshaller.setSchema(mySchema);
		unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		//
		// Open file
		final FileInputStream fis = new FileInputStream("src/test/resources/actionStatus.xml");

		JAXBElement<JkStatusType> jkActionStatusType = (JAXBElement<JkStatusType>) unmarshaller.unmarshal(fis);
		JkStatusType result = jkActionStatusType.getValue();
		System.out.println("Result: "+result.getResult().getType()+" "+result.getResult().getMessage());
	}
	/**
	 * 
	 * @throws JAXBException
	 */
	public void testMarshall() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("se.avegagroup.clustercontrol.data");
		ObjectFactory factory = new ObjectFactory();
		JkStatusType status = factory.createJkStatusType();
		JkServerType server = factory.createJkServerType();
		server.setName("localhost");
		status.setServer(server);
		JkResultType result = new JkResultType();
		result.setMessage("message");
		result.setType("type");
		status.setResult(result);
		JkBalancersType balancers = factory.createJkBalancersType();
		JkBalancerType balancer = factory.createJkBalancerType();
		JkMemberType jkMember = new JkMemberType(); 
		balancer.getMember().add(jkMember);
		JkMapType map = new JkMapType();
		balancer.getMap().add(map);
		JkSoftwareType software = factory.createJkSoftwareType();
		balancers.setBalancer(balancer);
		
		status.setBalancers(balancers);
		status.setSoftware(software);
		
		Schema mySchema;
		SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		try {
			URL url = getClass().getResource("/xsd/jkStatus.xsd");
		    File schemaFile = new File(url.toURI());
			mySchema = sf.newSchema( schemaFile );
		} catch( SAXException saxe ){
		    // ...(error handling)
		    mySchema = null;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			mySchema = null;
		}
		JAXBElement<JkStatusType> element = factory.createStatus(status);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setSchema(mySchema);
		m.marshal(element, System.out);
	}
}