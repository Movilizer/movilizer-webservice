package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


@RunWith(JUnit4.class)
public class MovilizerConfBuilderTest {
	private static long SYSTEM_ID = 0L;
	private static String PASSWORD = "";
	private static String cloudBaseAddress = "http://demo.movilizer.com/mds/";
	private MovilizerConfBuilder builder;
	private MovilizerRequest request;

	@Before
	public void setUp() throws Exception {
		builder = new MovilizerConfBuilder();
		request = new MovilizerRequest();
		request.setSystemId(SYSTEM_ID);
		request.setSystemPassword(PASSWORD);
	}

	@Test
	public void testSetCustomMdsUrl() throws Exception {
		builder.setEndpoint(cloudBaseAddress);
		assertThat(builder.getCloudBaseAddress(), is(URI.create(cloudBaseAddress)));
		MovilizerDistributionService service = builder.getService();
		MovilizerResponse response = service.getReplyFromCloudSync(request);
		assertThat(response, is(notNullValue()));
		assertThat(service.responseHasErrors(response), is(false));
	}

}
