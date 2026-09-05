package gudang;

import java.io.File;
import net.sf.jasperreports.engine.JasperCompileManager;

public class PengompilasiLaporan {
    public static void main(String[] args) {
        // Lokasi folder yang berisi file .jrxml
        File dir = new File("src/main/java/gudang");
        
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Folder tidak ditemukan: " + dir.getAbsolutePath());
            return;
        }

        // Ambil semua file yang berakhiran .jrxml
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".jrxml"));

        if (files == null || files.length == 0) {
            System.out.println("Tidak ada file .jrxml yang ditemukan di folder gudang.");
            return;
        }

        System.out.println("=========================================");
        System.out.println("MEMULAI BATCH KOMPILASI JASPERREPORTS");
        System.out.println("=========================================");

        int sukses = 0;
        int gagal = 0;

        for (File jrxmlFile : files) {
            String pathJrxml = jrxmlFile.getAbsolutePath();
            String pathJasper = pathJrxml.substring(0, pathJrxml.lastIndexOf('.')) + ".jasper";

            try {
                System.out.print("Mengompilasi: " + jrxmlFile.getName() + " -> ");
                JasperCompileManager.compileReportToFile(pathJrxml, pathJasper);
                System.out.println("BERHASIL!");
                sukses++;
            } catch (Exception e) {
                System.out.println("GAGAL!");
                System.err.println("   Error: " + e.getMessage());
                gagal++;
            }
        }

        System.out.println("=========================================");
        System.out.println("SELESAI: " + sukses + " Berhasil | " + gagal + " Gagal");
        System.out.println("=========================================");
    }
}