package net.thegreshams.model;

import static org.junit.Assert.*;
import net.thegreshams.model.Base;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestBase {
	
	final static String JSON = 
			"{" +
					"\"+optionalProp1\": \"This is an optional property with a +-sign.\"," +
					"\"mappedProp\": \"This is a mapped property.\"," +
					"\"optionalProp2\": \"This is an optional property without a +-sign.\"" +
			"}";

	private BaseTester baseTester;
	static class BaseTester extends Base {

		private static final long serialVersionUID = 1L;

		@JsonProperty( "mappedProp" )
		public String mappedProp;
		
	};
	

	@Before
	public void setUp() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		this.baseTester = mapper.readValue( JSON, BaseTester.class );
	}
	
	@Test
	public void testAnySetterForPropertyWithPlusSign() {
		
		assertEquals( "This is an optional property with a +-sign.", 
						this.baseTester.optionalProperties.get( "optionalProp1" ) );
	}
	
	@Test
	public void testAnySetterForPropertyWithoutPlusSign() {
		
		assertEquals( "This is an optional property without a +-sign.", 
						this.baseTester.optionalProperties.get( "optionalProp2" ) );
	}
	
	@Test
	public final void testAnySetterUsingNullKey() {
		
		this.baseTester.anySetter( null, "test" );
		assertTrue( !this.baseTester.optionalProperties.containsKey( null ) );		
	}
	
	@Test
	public final void testAnySetterUsingBlankKey() {
		
		this.baseTester.anySetter( " ", "test" );
		assertTrue( !this.baseTester.optionalProperties.containsKey( " " ) );		
	}
	
	@Test
	public final void testAnySetterUsingNullValue() {
		
		this.baseTester.anySetter( "test", null );
		assertTrue( !this.baseTester.optionalProperties.containsKey( "test" ) );		
	}
	
	@Test
	public final void testAnySetterUsingDuplicateKey() {
		
		assertTrue( this.baseTester.optionalProperties.containsKey( "optionalProp2" ) );
		this.baseTester.anySetter( "optionalProp2", "test" );
		assertEquals( "test", this.baseTester.optionalProperties.get( "optionalProp2" ) );	
	}
	
	@Test
	public final void testAnySetterUsingKeyWithWhitespaceAroundIt() {
		
		this.baseTester.anySetter( " testKey ", "test" );
		assertTrue( this.baseTester.optionalProperties.containsKey( "testKey" ) );
	}

	@Test
	public final void testMappedProp() {

		assertEquals( "This is a mapped property.", this.baseTester.mappedProp );
	}

}
