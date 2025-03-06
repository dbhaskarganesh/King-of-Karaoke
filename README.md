

### 🎤 **King of Karaoke** 🎶  
A **fun and interactive** music quiz app that challenges users to guess songs, artists, and albums based on lyrics and audio snippets!  


---

## 🚀 **Features**  
✅ **Single Player Mode** – Challenge yourself with exciting music trivia!  
✅ **Quiz Categories** – Guess the **song title, artist, or album** from lyrics and audio.  
✅ **Audio Integration** – Listen to song snippets to make your guess.  
✅ **Hint System** – Get image hints for tricky questions.  
✅ **Scoring & Leaderboard** – Earn points and compete with others!  
✅ **Music Playback** – Play or pause music while guessing.  
✅ **Auto Updates** – New songs added dynamically from an online database.  
✅ **Responsive UI** – Enjoy a sleek and intuitive design.  

---



## 📲 **Tech Stack**  
🔹 **Android (Java/Kotlin)** – Mobile development  
🔹 **Auth0** – Secure authentication  
🔹 **MediaPlayer API** – Audio playback  
🔹 **Glide** – Efficient image loading  
🔹 **JSONBin API** – Dynamic data updates  
🔹 **SDP & SSP Libraries** – Responsive UI scaling  

---

## 🛠 **Installation & Setup**  

### 🔹 Clone the Repository  
```sh
git clone https://github.com/yourusername/King-of-Karaoke.git
cd King-of-Karaoke
```

### 🔹 Open in Android Studio  
- Load the project in **Android Studio**  
- Sync Gradle dependencies  
- Run the app on an emulator or device  

---

## 🎮 **How to Play?**  
1️⃣ **Login** – Secure authentication with Auth0  
2️⃣ **Start Quiz** – Choose a category and begin  
3️⃣ **Guess the Song** – Use lyrics, audio clips, and hints  
4️⃣ **Earn Points** – Get rewards for correct answers  
5️⃣ **Check Leaderboard** – Compete with top players  

---

## 📝 **Code Snippet** (Play Music)  
```java
String audioUrl = questionInfo.getSong_file();
mediaPlayer = new MediaPlayer();
mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
try {
    mediaPlayer.setDataSource(audioUrl);
    mediaPlayer.prepare();
    mediaPlayer.start();
} catch (IOException e) {
    e.printStackTrace();
}
```

---

## 🔥 **Contributing**  
👥 Contributions are welcome! Fork the repo and submit a pull request.  

---

## 🛡 **License**  
📜 This project is licensed under the **MIT License**.  

---

## 📧 **Contact**  
📌 **Devalla Bhaskar Ganesh**  
✉️ [devallabhaskarganesh@gmail.com](mailto:devallabhaskarganesh@gmail.com)  
🔗 [LinkedIn](https://linkedin.com/in/devallabhaskarganesh/) | [GitHub](https://github.com/dbhaskarganesh)  

🎶 **Let’s make learning music more fun!** 🎶  
