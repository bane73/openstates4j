package org.openstates4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.openstates4j.model.Base;
import org.openstates4j.model.Bill;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BillTest {
	
	private Bill bill;
	
	@Before
	public void setUp() throws Exception {
		
		File billFile = new File( BillTest.class.getResource("/bill.json" ).getFile() );
		InputStream is 
			= new BufferedInputStream( new FileInputStream(billFile) );  
		byte[] buffer = new byte[(int) billFile.length()];  
		is.read(buffer);   
		is.close();  
	   
		String jsonBill = new String( buffer);
		ObjectMapper mapper = new ObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-08:00"));
		mapper.setDateFormat( sdf );
		this.bill = mapper.readValue( jsonBill, Bill.class );
	}
	
	@Test
	public void testBillExtendsBaseObject() {
		
		assertTrue( this.bill.getClass().getSuperclass() == Base.class );
	}
	
	@Test
	public void testToString() {
		assertEquals( "Bill [ (id:AB 667) (title:An act to amend Section 1750.1 " +
					  	"of the Business and Professions Code, and to amend Section " +
					  	"104830 of, and to add Section 104762 to, the Health and " +
					  	"Safety Code, relating to oral health.) (updated-at:Fri Jan " +
					  	"28 16:07:02 PST 2011) ]", 
					  this.bill.toString() );
	}
	
	@Test
	public void testId() {
		
		assertTrue( this.bill.id.equals( "AB 667" ) );
	}
	
	@Test
	public void testTitle() {
		assertTrue( this.bill.title.equals(
					"An act to amend Section 1750.1 of the Business and Professions " +
						"Code, and to amend Section 104830 of, and to add Section " +
						"104762 to, the Health and Safety Code, relating to oral health." ) );
	}
	
	@Test
	public void testAlternateTitles() {		
		List<String> alternateTitles = this.bill.alternateTitles;
		assertTrue( alternateTitles.size() == 3 );
		assertTrue( alternateTitles.get(0).equals( "Topical fluoride application." ) );
	}

}
