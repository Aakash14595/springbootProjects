package aakash.co.in.userjpa.filters;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class UserFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Log request details
        System.out.println("============INSIDE FILTER===============");
        System.out.println("========================================");
        System.out.println("Request URI: " + httpRequest.getRequestURI());
        System.out.println("Request Method: " + httpRequest.getMethod());

        // Continue the filter chain
        chain.doFilter(request, response);

        // Log response details
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        System.out.println("Response Status: " + httpResponse.getStatus());
    }
}


