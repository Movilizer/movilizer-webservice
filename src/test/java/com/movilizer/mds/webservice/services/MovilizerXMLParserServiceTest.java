package com.movilizer.mds.webservice.services;

import com.movilizer.mds.webservice.defaults.DefaultValues;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MovilizerXMLParserServiceTest extends MovilizerXMLParserServiceTestFixture {

    @Before
    public void setUp() throws Exception {
        xmlParserService = new MovilizerXMLParserServiceImpl(DefaultValues.OUTPUT_ENCODING);
    }
}
