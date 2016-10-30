package ru.ultimater;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    private static final int TWO_WEEKS = 14;
    private static final int MINUS_TWO_WEEKS = -14;
    SimpleDateFormat parseFormatter = new SimpleDateFormat("dd-MM-yyyy-HH:mm");
    SimpleDateFormat outputFormatter = new SimpleDateFormat("HH:mm");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        setContent(layout);

        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        layout.addComponent(horizontalLayout);
        horizontalLayout.setSpacing(true);
        List<CalendarEvent> events = createEvents();
        Calendar calendar = setupCalendar(events);


        TextArea textArea = setupEventList(events);

        Button previousMonth = getChangeMonthButton("Previous", MINUS_TWO_WEEKS, calendar);
        Button nextMonth = getChangeMonthButton("Next", TWO_WEEKS, calendar);

        horizontalLayout.addComponent(calendar);
        horizontalLayout.addComponent(textArea);

      //  horizontalLayout.setWidth("100%");
        final HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        layout.addComponent(horizontalLayout2);
        horizontalLayout2.setSpacing(true);

        layout.addComponent(horizontalLayout2);

        horizontalLayout2.addComponent(previousMonth);
        horizontalLayout2.addComponent(nextMonth);

        layout.setMargin(true);
        layout.setSpacing(true);

    }

    private Button getChangeMonthButton(String label, int days, Calendar calendar) {
        Button button = new Button(label);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        button.addClickListener(e -> {
            Date startDate, endDate;

            startDate = calendar.getStartDate();
            endDate = calendar.getEndDate();

            cal.setTime(startDate);
            cal.add(java.util.Calendar.DAY_OF_MONTH, days);
            calendar.setStartDate(cal.getTime());

            cal.setTime(endDate);
            cal.add(java.util.Calendar.DAY_OF_MONTH, days);
            calendar.setEndDate(cal.getTime());
        });

        return button;
    }

    private TextArea setupEventList(List<CalendarEvent> events) {
        TextArea textArea = new TextArea();
        textArea.setSizeFull();
        StringBuilder builder = new StringBuilder();
        events.forEach(e -> builder.append(e.getCaption()).append("\n"));
        String s = builder.toString();
        System.out.println("s = " + s);
        textArea.setValue(s);
        return textArea;
    }

    private List<CalendarEvent> createEvents() {
        List<CalendarEvent> events = new ArrayList<>();
        for(int i = 0; i < 10; i++)
            events.add(createEvent(i));

        return events;
    }

    private CalendarEvent createEvent(int i) {
        BasicEvent event;
        switch (i) {
            case 0:
                event = new BasicEvent("Training", "Technical with disc", getDateFromString("20-10-2016-21:00"), getDateFromString("20-10-2016-23:00"));
                break;
            case 1:
                event = new BasicEvent("Game", "Versus Easy", getDateFromString("22-10-2016-21:00"), getDateFromString("22-10-2016-23:00"));
                break;
            case 2:
                event = new BasicEvent("Boot camp", "Russian Women National Team. First day", getDateFromString("22-10-2016-9:00"), getDateFromString("22-10-2016-17:00"));
                break;
            case 3:
                event = new BasicEvent("Boot camp", "Russian Women National Team. Second day", getDateFromString("23-10-2016-9:00"), getDateFromString("23-10-2016-17:00"));
                break;
            case 4:
                event = new BasicEvent("Game", "Versus Flying Steps", getDateFromString("27-10-2016-21:00"), getDateFromString("27-10-2016-23:00"));
                break;
            case 5:
                event = new BasicEvent("Game", "Technical with disc", getDateFromString("30-10-2016-21:00"), getDateFromString("30-10-2016-23:00"));
                break;
            case 6:
                event = new BasicEvent("OFP", "Conditioning at Alexeyeva", getDateFromString("1-11-2016-19:30"), getDateFromString("1-11-2016-21:00"));
                break;
            case 7:
                event = new BasicEvent("Game", "Versus Flying Steps", getDateFromString("3-11-2016-21:00"), getDateFromString("3-11-2016-23:00"));
                break;
            case 8:
                event = new BasicEvent("Tournament", "Rigas Rudens. First day", getDateFromString("5-11-2016-10:30"), getDateFromString("5-11-2016-19:00"));
                break;
            case 9:
                event = new BasicEvent("Tournament", "Rigas Rudens. Second day", getDateFromString("6-11-2016-11:00"), getDateFromString("6-11-2016-16:00"));
                break;
            default:
                event = new BasicEvent("Gym", "Power development", getDateFromString("10-11-2016-8:00"), getDateFromString("10-11-2016-10:00"));
        }
        event.setStyleName("red-event");
        return event;
    }

    private Date getDateFromString(String stringDate) {
        try {
            return parseFormatter.parse(stringDate);
        } catch (ParseException ignore) {
            return new Date(0); // 1 jan 1970
        }
    }


    private Calendar setupCalendar(List<CalendarEvent> events) {
        Calendar calendar = new Calendar("My calendar");
        calendar.setWidth("80%");
        Date start = getDateFromString("20-10-2016-21:00");
        Date end = getDateFromString("10-11-2016-10:00");
        calendar.setStartDate(start);
        calendar.setEndDate(end);

        events.forEach(calendar::addEvent);

        CalendarEventProvider calendarEventProvider = calendar.getEventProvider();
        List<CalendarEvent> list = calendarEventProvider.getEvents(start, end);
        list.forEach(e -> System.out.println(e.getCaption() + "::" + e.getDescription()));

        calendar.setHandler(new CalendarComponentEvents.EventClickHandler() {
            @Override
            public void eventClick(CalendarComponentEvents.EventClick event) {
                CalendarEvent calendarEvent = event.getCalendarEvent();
                Window subWindow = new Window(calendarEvent.getCaption());
                VerticalLayout subContent = new VerticalLayout();
                subContent.setMargin(true);
                subWindow.setContent(subContent);

                // Put some components in it
                subContent.addComponent(new Label(calendarEvent.getDescription()));
                String start = outputFormatter.format(calendarEvent.getStart());
                String end = outputFormatter.format(calendarEvent.getEnd());
                subContent.addComponent(new Label(String.format("From %s till %s", start, end)));
                subContent.addComponent(new Button("Skip this event"));
                subWindow.setWidth("400px");
                subWindow.setHeight("200px");

                // Center it in the browser window
                subWindow.center();
                addWindow(subWindow);
            }
        });

        
        return calendar;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}