package com.tweetapp.tweetservice.utility;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tweetapp.tweetservice.constants.TweetAppConstants;
import com.tweetapp.tweetservice.dto.TweetExportDto;
import com.tweetapp.tweetservice.exception.TweetServiceException;

public class TweetPdfExportUtil {

	private List<TweetExportDto> exportDataList;

	private static final Color[] COLORS = { Color.WHITE, new Color(226, 246, 249, 50) };

	@Autowired
	public TweetPdfExportUtil(List<TweetExportDto> exportDataList) {
		this.exportDataList = exportDataList;
	}

	private void writeTableHeader(PdfPTable table) {
		String[] headers = TweetAppConstants.EXPORT_HEADER.split(",");

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.BLACK);
		font.setSize(12);

		for (String header : headers) {
			cell.setPhrase(new Phrase(header, font));
			table.addCell(cell);
		}

	}

	private void writeTableData(PdfPTable table) {

		PdfPCell cell = new PdfPCell();

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.DARK_GRAY);
		font.setSize(10);

		int iterationCount = 0;

		for (TweetExportDto exportData : exportDataList) {
			cell.setBackgroundColor(COLORS[iterationCount % 2]);
			iterationCount++;
			addRecordCell(exportData.getTweetMessage(), cell, table, font);
			addRecordCell(exportData.getTweetTopic(), cell, table, font);
			addRecordCell(exportData.getLikedBy(), cell, table, font);
			addRecordCell(exportData.getRepliedToTweetMsg(), cell, table, font);
			addRecordCell(exportData.getRepliedToTweetUser(), cell, table, font);
			addRecordCell(exportData.getCreatedDateTime() != null
					? DateUtils.userFriendlyFormat(exportData.getCreatedDateTime())
					: "-", cell, table, font);
			addRecordCell(exportData.getLastModifiedDateTime() != null
					? DateUtils.userFriendlyFormat(exportData.getLastModifiedDateTime())
					: "-", cell, table, font);

		}
	}

	private void addRecordCell(String data, PdfPCell cell, PdfPTable table, Font font) {
		cell.setPhrase(new Phrase(data != null ? data : "-", font));
		table.addCell(cell);
	}

	public void export(HttpServletResponse response) throws TweetServiceException {

		try (Document document = new Document(PageSize.A4)) {
			PdfWriter.getInstance(document, response.getOutputStream());
			document.open();

			Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			font.setSize(12);
			font.setColor(Color.BLACK);

			String title = "Tweet History - (As of " + DateUtils.userFriendlyFormat(LocalDateTime.now()) + ")";
			Paragraph p = new Paragraph(title, font);
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);

			PdfPTable table = new PdfPTable(7);
			table.setWidthPercentage(100f);
			table.setWidths(new float[] { 3.5f, 1.5f, 1.5f, 3.5f, 2f, 2f, 2f });
			table.setSpacingBefore(10);

			writeTableHeader(table);
			writeTableData(table);
			document.add(table);

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}

	}
}
