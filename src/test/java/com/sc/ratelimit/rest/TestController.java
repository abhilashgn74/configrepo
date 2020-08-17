package com.sc.ratelimit.rest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class TestController {

    @InjectMocks
    RateLimitController rateLimitController;
    
    @Test
    public void testViewItem() 
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
         
        Mockito.when(rateLimitController.viewItem(Mockito.anyString())).thenReturn("Transaction allowed for user Tom for API viewItem");
         
        String response = rateLimitController.viewItem("Tom");
         
        assertEquals("Transaction allowed for user Tom for API viewItem", response);
    }

}
