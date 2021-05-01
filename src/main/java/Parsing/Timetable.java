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

        String search = "Гл";
        String realSearch = "Глухих Михаил";
        String baseUrl = "https://ruz.spbstu.ru/";
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        Calendar from = new GregorianCalendar(2021, 4 , 28);
        Calendar to = new GregorianCalendar(2021, 5 , 20);
        ArrayList<Lesson> lessonList = new ArrayList<>();
        ArrayList<Teacher> teacherList = new ArrayList<>();
        String str = "";

        try {
            String searchUrl = baseUrl + "search/teacher?q=" + URLEncoder.encode(search, "UTF-8");
            HtmlPage page = client.getPage(searchUrl);
            List<HtmlElement> teachersList = (List<HtmlElement>) page.getByXPath(".//li[@class='search-result__item']");
            for (HtmlElement teacherH : teachersList) { //формирование списка преподов
                Teacher teacher = new Teacher();

                HtmlElement nameH = (HtmlElement) teacherH.getFirstByXPath(".//div[@class='search-result__title']");
                teacher.setName(nameH.asText());

                HtmlElement linkH = (HtmlElement) teacherH.getFirstByXPath(".//div[@class='search-result__title']/a");
                String str2 = linkH.toString();
                String[] bruh = str2.split("href=\"/");
                String[] meh = bruh[1].split("\" data");
                teacher.setLink(meh[0]);

                HtmlElement position = (HtmlElement) teacherH.getFirstByXPath(".//div[@class='search-result__comment']");
                teacher.setPosition(position.asText());

                teacherList.add(teacher);
            }
            for (Teacher teacher : teacherList) { //вывод на консоль преподов
                ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(teacher);
                System.out.println(jsonString); //вывод на консоль
            }
            //выбор нужного чела
            for (Teacher t : teacherList) {
                if (t.getName().contains(realSearch)) {
                    str = t.getLink();
                }
            }
            //переход на страницу препода
            while (from.before(to)) {
                String searchUrl2 = baseUrl + str + "?date=" + from.get(Calendar.YEAR) + "-" +
                        from.get(Calendar.MONTH) + "-" + from.get(Calendar.DAY_OF_MONTH);
                HtmlPage page2 = client.getPage(searchUrl2);
                List<HtmlElement> days = (List<HtmlElement>) page2.getByXPath(".//li[@class='schedule__day']");
                for (HtmlElement day : days) {
                    HtmlElement dateH = (HtmlElement) day.getFirstByXPath(".//div[@class='schedule__date']");
                    String date = dateH.asText();
                    List<HtmlElement> lessons = (List<HtmlElement>) day.getByXPath(".//li[@class='lesson']");
                    for (HtmlElement lesson : lessons) {
                        Lesson lessonX = new Lesson();

                        HtmlElement subject = (HtmlElement) lesson.getFirstByXPath(".//div[@class='lesson__subject']");
                        lessonX.setName(subject.asText().substring(11));
                        lessonX.setTime(subject.asText().substring(0, 11));

                        HtmlElement type = (HtmlElement) lesson.getFirstByXPath(".//div[@class='lesson__type']");
                        lessonX.setType(type.asText());

                        List<HtmlElement> groupsH = (List<HtmlElement>) lesson.getByXPath(".//span[@class='lesson__group']");
                        ArrayList<String> groups = new ArrayList<String>();
                        for (HtmlElement group : groupsH) {
                            groups.add(group.asText());
                        }
                        lessonX.setGroups(groups);

                        HtmlElement teachers = (HtmlElement) day.getFirstByXPath(".//div[@class='lesson__teachers']");
                        lessonX.setTeachers(teachers.asText());

                        HtmlElement place = (HtmlElement) day.getFirstByXPath(".//div[@class='lesson__places']");
                        lessonX.setPlace(place.asText());
                        lessonX.setDay(date);
                        lessonList.add(lessonX);
                    }
                }
                from.add(Calendar.DAY_OF_MONTH, 7); //переход на некст неделю
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Lesson lesson : lessonList) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(lesson);
            System.out.println(jsonString); //вывод на консоль
        }
    }
}
