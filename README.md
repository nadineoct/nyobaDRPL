# 📔 JuKi - Aplikasi Jurnal Pribadi

JuKi adalah aplikasi pencatatan jurnal harian yang dibangun menggunakan **Java**, antarmuka grafis **JavaFX**, dan database **SQLite**.

## 🛠️ Prasyarat (Prerequisites)
Sebelum menjalankan program di laptop lain, pastikan sistem sudah memiliki:
1. **Java Development Kit (JDK)** versi 11 atau yang lebih baru (disarankan JDK 17 atau 21).
2. IDE Java yang mendukung JavaFX (seperti **IntelliJ IDEA**, **Visual Studio Code**, atau **Eclipse**).
3. **Maven** atau **Gradle** (sesuai dengan sistem *build tool* yang digunakan di project ini).

## 🚀 Cara Menjalankan Program

### Opsi 1: Menggunakan IDE (Paling Mudah)
1. Salin seluruh folder project `nyobaDRPL` ke laptop baru (atau `git clone` jika menggunakan GitHub).
2. Buka IDE favoritmu (sangat disarankan menggunakan **IntelliJ IDEA**).
3. Pilih **Open** dan pilih folder `nyobaDRPL`.
4. Tunggu beberapa saat agar IDE mengenali struktur *project* dan mengunduh *dependencies* (JavaFX dan library SQLite).
5. Buka panel *Project Explorer*, lalu navigasikan ke file utama di:
   `src/main/java/com/juki/MainApp.java`
6. Klik kanan pada file tersebut dan pilih **Run 'MainApp.main()'** (atau klik tombol *play* warna hijau di sebelah baris kode `public static void main`).

### Opsi 2: Menjalankan Lewat Terminal / CMD
Buka terminal atau *command prompt*, pastikan kamu sudah berada di dalam folder project `nyobaDRPL`, lalu jalankan salah satu perintah berikut (tergantung *build tool* project kamu):

**Jika menggunakan Maven:**
```bash
mvn clean javafx:run
```

**Jika menggunakan Gradle:**
```bash
# Di OS Windows:
gradlew.bat run

# Di macOS / Linux:
./gradlew run
```

## 🗄️ Tentang Database
Aplikasi ini menggunakan **SQLite**. Kamu **tidak perlu** repot menginstal XAMPP, MySQL, atau mengonfigurasi server database secara manual. 
File database akan **otomatis dibuat** saat aplikasi pertama kali dijalankan melalui *class* `DatabaseHelper`.