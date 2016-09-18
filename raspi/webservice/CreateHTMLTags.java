/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package raspi.webservice;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Klasse zum Erzeugen von HTML-Tags.
 * 
 * @author Wolfgang Höfer 
 * @version 1.0
 */
public class CreateHTMLTags {

    public static final String MONOSPACED = "\"Courier New\", Courier, monospace";
    public static final String SANS_SERIF = "Arial, Helvetica, sans-serif";
    public static final String SERIF = "\"Times New Roman\", Times, serif";
    public static final String DIALOG = MONOSPACED;
    public static final String DIALOG_INPUT = MONOSPACED;

    private static void createParameterLine(Map<String, String> map, StringBuilder strBuilder) {
        if (map != null) {
            Set<Entry<String, String>> entrySet = map.entrySet();
            for (Iterator<Entry<String, String>> it = entrySet.iterator(); it.hasNext();) {
                Entry<String, String> entry = it.next();
                strBuilder.append(entry.getKey());
                strBuilder.append("=\"");
                strBuilder.append(entry.getValue());
                strBuilder.append("\" ");
            }
        }
    }

    public static void createStartTag(String name, Map<String, String> map, StringBuilder strBuilder) {
        strBuilder.append("<");
        strBuilder.append(name);
        if (map != null) {
            strBuilder.append(" ");
            createParameterLine(map, strBuilder);
            strBuilder.append(" >\n");
        } else {
            strBuilder.append(">\n");
        }
    }

    public static void createStoppTag(String name, StringBuilder strBuilder) {
        strBuilder.append("</");
        strBuilder.append(name);
        strBuilder.append(">\n");
    }

    public static void createCloseTag(String name, Map<String, String> map, StringBuilder strBuilder) {
        strBuilder.append("<");
        strBuilder.append(name);
        strBuilder.append(" ");
        createParameterLine(map, strBuilder);
        strBuilder.append(" />\n");
    }

    public static void createColorToHtml(Color c, StringBuilder strBuilder) {
        if (c != null) {
            strBuilder.append(": #");
            strBuilder.append(Integer.toHexString(c.getRed()));
            strBuilder.append(Integer.toHexString(c.getGreen()));
            strBuilder.append(Integer.toHexString(c.getBlue()));
        }
    }

/*    public static void createBgColorToHtml(JWebComponent comp, StringBuilder strBuilder) {
        if (comp != null && comp.getBackground() != null) {
            strBuilder.append("background-color");
            createColorToHtml(comp.getBackground(), strBuilder);
            strBuilder.append("; ");
        }
    }

    public static void createFgColorToHtml(JWebComponent comp, StringBuilder strBuilder) {
        if (comp != null && comp.getForeground() != null) {
            strBuilder.append("color");
            createColorToHtml(comp.getForeground(), strBuilder);
            strBuilder.append("; ");
        }
    }
*/
    public static void createFontToHtml(Font f, StringBuilder strBuilder) {
        if (f != null) {
            String name = f.getName();
            if (name.equals(Font.DIALOG)) {
                strBuilder.append("font-family: ");
                strBuilder.append(DIALOG);
                strBuilder.append("; ");
            } else if (name.equals(Font.DIALOG_INPUT)) {
                strBuilder.append("font-family: ");
                strBuilder.append(DIALOG_INPUT);
                strBuilder.append("; ");
            } else if (name.equals(Font.MONOSPACED)) {
                strBuilder.append("font-family: ");
                strBuilder.append(MONOSPACED);
                strBuilder.append("; ");
            } else if (name.equals(Font.SANS_SERIF)) {
                strBuilder.append("font-family: ");
                strBuilder.append(SANS_SERIF);
                strBuilder.append("; ");
            } else if (name.equals(Font.SERIF)) {
                strBuilder.append("font-family: ");
                strBuilder.append(SERIF);
                strBuilder.append("; ");
            }
            int style = f.getStyle();
            if (style == Font.BOLD) {
                strBuilder.append("font-weight: bold; ");
            } else if (style == Font.ITALIC) {
                strBuilder.append("font-style: italic; ");
            } else if (style == Font.PLAIN) {
                strBuilder.append("font-weight: normal; ");
            }
            Integer pointsize = new Integer(f.getSize());
            strBuilder.append("font-size: ");
            strBuilder.append(pointsize.toString());
            strBuilder.append("pt; ");
        }
    }

    public static void createBoundsToHtml(Rectangle r, StringBuilder strBuilder) {
        if(r != null){
            strBuilder.append("width: ");
            strBuilder.append(String.valueOf((int) r.getWidth()));
            strBuilder.append("px ;height: ");
            strBuilder.append(String.valueOf((int) r.getHeight()));
            strBuilder.append("px ;left: ");
            strBuilder.append(String.valueOf((int) r.getX()));
            strBuilder.append("px ;top: ");
            strBuilder.append(String.valueOf((int) r.getY()));
            strBuilder.append("px ;");
        }
    }

/*    public static void createStyleFromComponent(JWebComponent comp, StringBuilder strBuilder) {
        strBuilder.append("position: absolute;");
        createBgColorToHtml(comp, strBuilder);
        createFgColorToHtml(comp, strBuilder);
        createFontToHtml(comp.getFont(), strBuilder);
        createBoundsToHtml(comp.getBounds(), strBuilder);
    }
*/
    public static void main(String[] args) {
     /*   StringBuilder strBuilder = new StringBuilder();
        JWebComponent jWebComponent = new JWebComponent();
        jWebComponent.setBounds(5, 65, 100, 20);
        jWebComponent.setBackground(Color.GRAY);
        jWebComponent.setForeground(Color.BLACK);
        StringBuilder style = new StringBuilder();
        createStyleFromComponent(jWebComponent, style);
        Map map = new HashMap();
        map.put("id", "IDTexteingabe");
        map.put("type", "text");
        map.put("name", "Texteingabe1");
        map.put("style", style.toString());
        createCloseTag("input", map, strBuilder);
        System.out.println(strBuilder.toString());*/

    }
}
