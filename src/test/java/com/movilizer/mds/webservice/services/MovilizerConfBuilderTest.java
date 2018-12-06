package com.movilizer.mds.webservice.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(JUnit4.class)
public class MovilizerConfBuilderTest {

    private MovilizerConfBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new MovilizerConfBuilder();
    }

    @Test
    public void testSetURLWithPort() throws Exception {
        builder.setEndpoint("http://10.10.0.8:8018/mds");
        MovilizerDistributionService service = builder.getService();
        assertThat(builder.getCloudBaseAddress().toString(), is("http://10.10.0.8:8018/mds"));
    }
}
