package com.ashley.englishpage;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Rick
 * @createdAt 2021-02-22 20:06:00
 */
public class WordCreator {

    private String sourceFolder;

    public WordCreator(String sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public void create(Article[] articles) throws Exception {
        File parent =  new File(sourceFolder);
        if (!parent.exists()) {
            parent.mkdir();
        }

        Article blank = articles[0];
        Article answer = articles[1];

        XWPFDocument document = new XWPFDocument();
        FileOutputStream out = new FileOutputStream(new File(parent, blank.getTitle() + ".docx"));
        createHeader(document);
        writeArticle(document, blank);
        writeArticle(document, answer);

        document.write(out);
        out.close();
    }

    public void createHeader(XWPFDocument doc) {
        CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(doc, sectPr);

        //write header content
        CTP ctpHeader = CTP.Factory.newInstance();
//        CTR ctrHeader = ctpHeader.addNewR();
//        CTText ctHeader = ctrHeader.addNewT();
//        String headerText = "It is never too late to study.";
//        ctHeader.setStringValue(headerText);

        XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, doc);
        XWPFRun run = headerParagraph.createRun();
        run.setText("It is never too late to study.");
        run.setTextHighlightColor("darkGreen");
        run.setColor("ffffff");

        headerParagraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFParagraph[] parsHeader = new XWPFParagraph[1];
        parsHeader[0] = headerParagraph;
        policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);

    }

    private void writeArticle(XWPFDocument document, Article article) {
        write(document, ParagraphAlignment.CENTER, 28, article.getTitle(), paragraph -> {paragraph.setPageBreak(true);}, run -> {
            run.setBold(true);
        });

        if (StringUtils.isNotBlank(article.getSubtitle())) {
            write(document, ParagraphAlignment.CENTER, 18, article.getSubtitle(), run -> {
                run.setBold(true);
                run.setItalic(true);
            });
        }

        writeLine(document, 1);
        final String answerDescription = "Keys";
        write(document, ParagraphAlignment.LEFT, 14, article.getDescription(), null, run -> {
            if (answerDescription.equals(article.getDescription())) {
                run.setBold(true);
            }
        });
        writeLine(document, 1);

        List<String> items = article.getItems();
        int size = items.size();
        for (int i = 0; i < size; i++) {
            String item = items.get(i);
            write(document, ParagraphAlignment.LEFT, 14, item);
            if (i != size - 1) {
                writeLine(document);
            }
        }
    }

    private void write(XWPFDocument document, ParagraphAlignment alignment, int fontsize, String text) {
        write(document, alignment, fontsize, text, null, null);
    }

    private void write(XWPFDocument document, ParagraphAlignment alignment, int fontsize, String text, Consumer<XWPFRun> runConsumer) {
        write(document, alignment, fontsize, text, null, runConsumer);
    }

    private void write(XWPFDocument document, ParagraphAlignment alignment, int fontsize, String text, Consumer<XWPFParagraph> paragraphConsumer, Consumer<XWPFRun> runConsumer) {
        //create Paragraph
        XWPFParagraph paragraph = document.createParagraph();
        // 设置行高
        paragraph.setSpacingBetween(1.5);
        if (Objects.nonNull(paragraphConsumer)) {
            paragraphConsumer.accept(paragraph);
        }

        paragraph.setAlignment(alignment);

        XWPFRun root = null;

        if (StringUtils.isNotBlank(text)) {
            StringBuilder writeText = new StringBuilder();
            char[] chars = text.toCharArray();
            boolean bold = false;
            for (int i = 0; i < chars.length; i++) {
                char curChar = chars[i];
                if (curChar == '*') {
                    XWPFRun run = paragraph.createRun();
                    run.setText(writeText.toString());
                    run.setFontSize(fontsize);
                    writeText.delete(0, writeText.length());
                    if (bold) {
                        bold = false;
                        run.setBold(true);
                    } else {
                        bold = true;
                    }
                } else {
                    writeText.append(curChar);
                }
            }

            if (StringUtils.isNotBlank(writeText.toString())) {
                root = paragraph.createRun();
                root.setText(writeText.toString());
                root.setFontSize(fontsize);
            }
        }

        if (Objects.nonNull(runConsumer)) {
            runConsumer.accept(root);
        }
    }

    private void writeLine(XWPFDocument document){
        writeLine(document, 1);
    }

    private void writeLine(XWPFDocument document, int num){
        for (int i = 0; i < num; i++) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(null);
        }
    }
}
