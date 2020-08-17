package com.sc.ratelimit.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
public class RateLimitControllerTest {

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
