package com.xyz.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.xyz.pojo.StockDaily;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class PDFUtil {

        public static final String[] tableHeader= { "代码", "名字",
                "概念", "行业", "涨跌幅", "日期", "收盘价", "净利润增长", "历史日期", "历史收盘价", "股东"};

        // 表格的设置
        private static final int spacing = 2;

        // 表格的设置
        private static final int padding = 4;

        // 导出Pdf文挡
        public static void exportPdfDocument(Map<String, List<StockDaily>> mailDetails) {
            // 创建文Pdf文挡50, 50, 50,50左右上下距离
            Document document = new Document(new Rectangle(1500, 2000), 50, 50, 50,
                    50);
            try {
                //使用PDFWriter进行写文件操作
                PdfWriter.getInstance(document,new FileOutputStream(
                        "/tmp/stock.pdf"));
                document.open();
                // 中文字体
                BaseFont bfChinese =BaseFont.createFont("STSong-Light",
                        "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
                Font fontChinese = new Font(bfChinese, 12, Font.NORMAL);

                document.addTitle("Stocks");

                for(String key : mailDetails.keySet()){
                    addDesc(document, bfChinese, key);
                    List<StockDaily> stocks = mailDetails.get(key);
                    addTable(stocks, document, bfChinese, fontChinese);
                    document.newPage();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            document.close();
        }

    private static void addTable(List<StockDaily> stocks, Document document, BaseFont bfChinese, Font fontChinese) throws DocumentException {
        // 创建有colNumber(6)列的表格
        PdfPTable datatable = new PdfPTable(tableHeader.length);
        //定义表格的宽度
        int[] cellsWidth = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        datatable.setWidths(cellsWidth);
        // 表格的宽度百分比
        datatable.setWidthPercentage(100);
        datatable.getDefaultCell().setPadding(padding);
        datatable.getDefaultCell().setPaddingBottom(padding);
        //datatable.getDefaultCell().setBorderWidth(spacing);
        //设置表格的底色
        datatable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        datatable.getDefaultCell().setHorizontalAlignment(
                Element.ALIGN_CENTER);
        // 添加表头元素
        for (int i = 0; i <tableHeader.length; i++) {
            datatable.addCell(new Paragraph(tableHeader[i], new Font(bfChinese, 13, Font.BOLD)));
        }
        // 添加子元素
        for (StockDaily stock : stocks) {
            //stock.setProfit(12.1f);
            //stock.setName("da");
            datatable.addCell(new Paragraph(stock.getTsCode(), fontChinese));
            datatable.addCell(new Paragraph(stock.getName(), fontChinese));
            datatable.addCell(new Paragraph(stock.getConcepts(), fontChinese));
            datatable.addCell(new Paragraph(stock.getIndustry(), fontChinese));
            datatable.addCell(new Paragraph(stock.getTotalPctChg() == null ? "" : stock.getTotalPctChg().toString() + "%", fontChinese));
            datatable.addCell(new Paragraph(stock.getTradeDate(), fontChinese));
            datatable.addCell(new Paragraph(stock.getClosePrice() == null ? "" : stock.getClosePrice().toString(), fontChinese));
            datatable.addCell(new Paragraph(stock.getProfit() == null ? "" : stock.getProfit().toString() + "%", fontChinese));
            datatable.addCell(new Paragraph(stock.getTradeDateHist(), fontChinese));
            datatable.addCell(new Paragraph(stock.getClosePriceHist() == null ? "" : stock.getClosePriceHist().toString(), fontChinese));
            datatable.addCell(new Paragraph(stock.getHolders(), fontChinese));
        }
        document.add(datatable);
    }

    private static void addDesc(Document document, BaseFont bfChinese, String title) throws DocumentException {
        Paragraph desc = new Paragraph(title, new Font(bfChinese, 20, Font.BOLD));
        desc.setAlignment(Element.ALIGN_CENTER);
        desc.setSpacingAfter(20);
        desc.setSpacingBefore(20);
        document.add(desc);
    }

}
