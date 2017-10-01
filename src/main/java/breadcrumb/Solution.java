package breadcrumb;

import java.util.*;

/**
 * Created by alex on 1/10/17.
 */
public class Solution {

    private static final Set<String> WORDS_TO_IGNORE = new HashSet<>(Arrays.asList(new String[] {"the","of","in","from","by","with","and", "or", "for", "to", "at", "a"}));

    private static class Link {
        private String path;
        private String text;

        public Link(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }

    public static String generate_bc(String url, String separator) {
        System.out.println("*"+url+"*");
        String removedProtocol = url.replaceFirst("https://", "").replaceFirst("http://", "");

        String[] split = removedProtocol.split("/");

        // Filter each section
        List<Link> sections = new ArrayList<>();
        for (String s : split) {
            if (!s.isEmpty() && !s.startsWith("index.")) {
                if (sections.isEmpty()) {
                    sections.add(new Link("", "HOME"));
                } else {
                    String text = treatText(s);
                    if (!text.isEmpty()) {
                        sections.add(new Link(s, text));
                    }
                }
            }
        }

        // Create breadcrumb
        List<String> breadcrumb = new ArrayList<>();
        String path = "";
        for (int i = 0; i < sections.size(); i++) {
            Link link = sections.get(i);
            path = path + link.path + "/";
            if (i == sections.size() - 1) {
                breadcrumb.add("<span class=\"active\">" + link.text + "</span>");
            } else {
                breadcrumb.add("<a href=\"" + path + "\">" + link.text + "</a>");
            }
        }

        return breadcrumb.stream().reduce((s1, s2) -> s1 +  separator + s2).get();
    }

    private static String treatText(String text) {
        if (text == null) { return "HOME"; }
        String result = text.toUpperCase();
        int i = result.indexOf(".");
        if (i != -1) {
            result = result.substring(0, i);
        }
        i = result.indexOf("?");
        if (i != -1) {
            result = result.substring(0, i);
        }
        i = result.indexOf("#");
        if (i != -1) {
            result = result.substring(0, i);
        }
        if (result.length() > 30) {
            String[] words = result.split("-");
            StringBuilder sb = new StringBuilder();
            for (String word : words) {
                if (!WORDS_TO_IGNORE.contains(word.toLowerCase())) {
                    sb.append(word.charAt(0));
                }
            }
            result = sb.toString();
        } else {
            result = result.replaceAll("-", " ");
        }
        return result;
    }
}
