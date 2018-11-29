package javaUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class FileReader {

	private static int rigaInte = Resources.rigaIntestazione;
	private static int rigaColle = Resources.rigaCollegamento;
	private static int rigaAccod = Resources.rigaAccodamento;
	private static int rigaLim = Resources.rigaLimite;
	private static String filePath = Resources.path;

	public static synchronized ArrayList<ArrayList<String>> fillTheSheet(InputStream streamStarter, String daSaltare)
			throws IOException {

		Workbook workbook = WorkbookFactory.create(streamStarter);
		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();

		ArrayList<ArrayList<String>> sheetDataStarter = new ArrayList<ArrayList<String>>();
		ArrayList<String> riga = new ArrayList<String>();
		Cell cellHelp;

		String need = null;
		int z = 0;

		int indexRiga = 1;
		for (Row row : sheet) {
			if (!daSaltare.contains(String.valueOf(indexRiga))) {
				for (z = 0; z < row.getPhysicalNumberOfCells(); z++) {

					if (row.getCell(z) == null) {
						cellHelp = null;
					} else {
						cellHelp = row.getCell(z);
					}

					need = dataFormatter.formatCellValue(cellHelp);

					if (cellHelp == null) {
						riga.add("");
					} else {
						riga.add(need);
					}
				}
				sheetDataStarter.add(riga);
			}
			riga = new ArrayList<String>();
			indexRiga++;
		}
		workbook.close();

		return sheetDataStarter;
	}

	public static synchronized Map<String, String> readTheModel(InputStream streamModel,
			ArrayList<ArrayList<String>> sheetDataStarter, String newFileName) throws IOException {

		Map<String, String> esito = new HashMap<String, String>();
		boolean esitoFunzione = true;

		Workbook workbook = WorkbookFactory.create(streamModel);
		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();

		ArrayList<String> intestazione = new ArrayList<String>();
		ArrayList<String> collegamento = new ArrayList<String>();
		ArrayList<String> accodamento = new ArrayList<String>();
		ArrayList<String> limite = new ArrayList<String>();

		Cell cellHelp;

		String need = null;
		int z = 0;

		int indexRiga = 1;
		for (Row row : sheet) {
			if (indexRiga == rigaInte || indexRiga == rigaColle || indexRiga == rigaAccod || indexRiga == rigaLim) {
				for (z = 0; z < row.getPhysicalNumberOfCells(); z++) {
					if (row.getCell(z) == null) {
						cellHelp = null;
					} else {
						cellHelp = row.getCell(z);
					}

					need = dataFormatter.formatCellValue(cellHelp);
					if (indexRiga == rigaInte) {
						if (cellHelp == null) {
							intestazione.add("");
						} else {
							intestazione.add(need);
						}
					} else if (indexRiga == rigaColle) {
						if (cellHelp == null) {
							collegamento.add("");
						} else {
							collegamento.add(need);
						}
					} else if (indexRiga == rigaAccod) {
						if (cellHelp == null) {
							accodamento.add("");
						} else {
							accodamento.add(need);
						}
					} else if (indexRiga == rigaLim) {
						if (cellHelp == null) {
							limite.add("");
						} else {
							limite.add(need);
						}
					}
				}
			}
			indexRiga++;
		}
		workbook.close();
		System.out.println("inteModel: " + intestazione.size());
		System.out.println("collegamento: " + collegamento.size());
		System.out.println("accodamento: " + accodamento.size());
		System.out.println("limite: " + limite.size());
		if (sheetDataStarter.size() != 0 && intestazione.size() != 0 && collegamento.size() != 0
				&& accodamento.size() != 0 && limite.size() != 0 && newFileName != null) {
			esito = createTheSheet(sheetDataStarter, intestazione, collegamento, accodamento, limite, newFileName);
		} else {
			esito.put("false", "controllare i file e riprovare");
		}

		return esito;
	}

	public static synchronized Map<String, String> createTheSheet(ArrayList<ArrayList<String>> sheetStarter,
			ArrayList<String> intest, ArrayList<String> collega, ArrayList<String> accoda, ArrayList<String> limit,
			String fileName) throws IOException {

		Map<String, String> esitoFun = new HashMap<String, String>();
		Workbook wb = new HSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet("Caricamento");

		// riga intestazione
		Row row = sheet.createRow(0);
		for (int i = 0; i < intest.size(); i++) {
			row.createCell(i).setCellValue(intest.get(i));
		}

		int indexStarterRow = 0;
		for (int i = 1; i < sheetStarter.size(); i++) {

			Row newRow = sheet.createRow(i);
			for (int col = 0; col < intest.size(); col++) {

				int colle = Integer.parseInt(collega.get(col));
				int limite = Integer.parseInt(limit.get(col));
				int acc = Integer.parseInt(accoda.get(col));
				String accodValue="";
				if (acc != 0) {
					acc--;
					accodValue = (String) ((ArrayList) sheetStarter.get(indexStarterRow)).get(acc);
				}else {
					accodValue = " ";
				}
				if (colle != 0) {
					colle--;
					String cellValue = (String) ((ArrayList) sheetStarter.get(indexStarterRow)).get(colle);
					if (limite == 0) {
						newRow.createCell(col).setCellValue(
								cellValue + " " +accodValue);
					} else if (cellValue.length() <= limite) {
						newRow.createCell(col).setCellValue(
								cellValue + " " + accodValue);
					} else {
						colle++;
						esitoFun.put("false", "limite sforato nella riga: " + i + ", colonna: " + colle);
						return esitoFun;
					}
				}
//				System.out.print(colle);
			}
			indexStarterRow++;
//			System.out.println();
		}

		// creazione file
		Path path = Paths.get(filePath);
		if (!Files.exists(path)) {

			Files.createDirectory(path);
			System.out.println("Directory created");
		} else {

			System.out.println("Directory already exists");
		}
		FileOutputStream fileOut = new FileOutputStream(filePath + fileName + ".xls");
		wb.write(fileOut);
		fileOut.close();
		wb.close();
		esitoFun.put("true", "");

		return esitoFun;
	}

}
