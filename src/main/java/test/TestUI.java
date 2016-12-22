package test;

import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import test.spring.TestBean;


@SuppressWarnings("serial")
@SpringUI
@Theme("reindeer")
@PreserveOnRefresh
public class TestUI extends UI {

	private final static Logger LOG = Logger.getLogger(TestUI.class.getName());
	private final static int POLL_INTERVAL = 1000;
	private VerticalLayout layout;
	private Component component;
	@Autowired
	private TestBean testBean;

	@WebServlet(value = "/", name="Servlet", asyncSupported = true)	
	public static class Servlet extends SpringVaadinServlet {

	}
	@WebListener
    public static class MyContextLoaderListener extends ContextLoaderListener {
    }
	@Configuration
	@EnableVaadin
	public static class MyConfiguration {		
	}
	public TestUI() {
		initComponents();
	}

	@Override
	protected void init(VaadinRequest request) {
		ServletContext context = VaadinServlet.getCurrent().getServletContext();

    	ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
    	testBean = appContext.getBean(TestBean.class);
		setSettings();
		getSettings();
		initListeners();
	}

	private void initListeners() {
		addDetachListener(new DetachListener() {

			@Override
			public void detach(DetachEvent event) {
				releaseResources();
			}
		});
		this.getPage().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {

			@Override
			public void browserWindowResized(BrowserWindowResizeEvent event) {
			}
		});
	}

	@Override
	protected void refresh(VaadinRequest request) {
		try {
		} catch (Exception e) {
			Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
		}
	}

	private void initComponents() {
		layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setSpacing(false);
		
		layout.addStyleName("backcolor");
		setContent(layout);
		Button button = new Button("Click Me");		
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
            	try {
            		String message = testBean.getSelect((int)(System.currentTimeMillis() % 3));
            		Notification.show(message);
            	}catch(Exception e) {
            		Notification.show(e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
            	}
            }
			
        });
        layout.addComponent(button);        
        layout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
		layout.setSizeFull();
	}

	public void setComponent(Component component) {
		if (this.component != null) {
			layout.removeComponent(this.component);
		}
		if (component != null) {
			layout.addComponent(component);
			layout.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
		}
		this.component = component;
	}

	private void getSettings() {

	}

	private void setSettings() {
		this.setPollInterval(POLL_INTERVAL);
		this.addPollListener(new PollListener() {

			@Override
			public void poll(PollEvent event) {
			}
		});
	}

	private void releaseResources() {

		try {
		} catch (Exception e) {
			LOG.throwing(TestUI.class.getName(), "detach", e);
		}

	}
}