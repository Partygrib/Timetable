package Parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Timetable {
    public static void main(String[] args) throws JsonProcessingException {

        String searchQuery = "Кошелев Сергей";
        String baseUrl = "https://ruz.spbstu.ru/";
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        Calendar calendar1 = new GregorianCalendar(2021, 4 , 28);
        Calendar calendar2 = new GregorianCalendar(2021, 5 , 20);
        ArrayList<Lesson> list = new ArrayList<Lesson>();
        try {
            //выбор препода
            String searchUrl = baseUrl + "search/teacher?q=" + URLEncoder.encode(searchQuery, "UTF-8");
            HtmlPage page = client.getPage(searchUrl);
            HtmlElement item1 = (HtmlElement) page.getFirstByXPath("//div/div/div/ul/li/div/a");
            String str2 = item1.toString();
            String[] bruh = str2.split("href=\"/");
            String[] bruh1 = bruh[1].split("\" data");
            String str3 = bruh1[0];
            //переход на страницу препода
            while (calendar1.before(calendar2)) {
                String searchUrl2 = baseUrl + str3 + "?date=" + calendar1.get(Calendar.YEAR) + "-" +
                        calendar1.get(Calendar.MONTH) + "-" + calendar1.get(Calendar.DAY_OF_MONTH);
                HtmlPage page2 = client.getPage(searchUrl2);
                List<HtmlElement> items = (List<HtmlElement>) page2.getByXPath(".//li[@class='schedule__day']");
                for (HtmlElement htmlItem1 : items) {
                    HtmlElement dayH = (HtmlElement) htmlItem1.getFirstByXPath(".//div[@class='schedule__date']");
                    String day = dayH.asText();
                    System.out.println("День: " + day);
                    List<HtmlElement> items2 = (List<HtmlElement>) htmlItem1.getByXPath(".//li[@class='lesson']");
                    for (HtmlElement htmlItem2 : items2) {
                        Lesson lesson = new Lesson();
                        HtmlElement whatH = (HtmlElement) htmlItem2.getFirstByXPath(".//div[@class='lesson__subject']");
                        String what = whatH.asText();
                        lesson.setName(what.substring(11));
                        lesson.setTime(what.substring(0, 11));
                        System.out.println("Предмет: " + what.substring(11));
                        System.out.println("Время: " + what.substring(0, 11));

                        HtmlElement typeH = (HtmlElement) htmlItem2.getFirstByXPath(".//div[@class='lesson__type']");
                        String type = typeH.asText();
                        lesson.setType(type);
                        System.out.println("Тип пары: " + type);

                        HtmlElement groupH = (HtmlElement) htmlItem2.getFirstByXPath(".//span[@class='lesson-groups__additional']");
                        String group = groupH.asText();

                        HtmlElement groupsH = (HtmlElement) htmlItem2.getFirstByXPath(".//div[@class='lesson-groups__list']");
                        String groups = groupsH.asText();
                        lesson.setGroups(groups);
                        System.out.println(groups);

                        HtmlElement teachersH = (HtmlElement) htmlItem2.getFirstByXPath(".//div[@class='lesson__teachers']");
                        String teachers = teachersH.asText();
                        lesson.setTeachers(teachers);
                        System.out.println("Преподы: " + teachers);

                        HtmlElement gdeH = (HtmlElement) htmlItem2.getFirstByXPath(".//div[@class='lesson__places']");
                        String gde = gdeH.asText();
                        lesson.setPlace(gde);
                        System.out.println("Место: " + gde);
                        System.out.println();
                        lesson.setDay(day);
                        list.add(lesson);
                    }
                }
                calendar1.add(Calendar.DAY_OF_MONTH, 7);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Lesson lesson : list) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(lesson);
            System.out.println(jsonString);
        }
    }
}
