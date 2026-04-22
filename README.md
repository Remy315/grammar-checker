# Grammar Checker Android App 📱

A smart grammar checking application with floating window accessibility and dual-mode operation.

## 🌟 Features

### 🔧 Core Functionality
- **Floating Window**: Access grammar checking from any app
- **Dual Modes**: Practice mode and Auto-correct mode
- **Smart Detection**: Identifies common grammar errors
- **Visual Feedback**: Color-coded error highlighting
- **Language Toggle**: English/Chinese interface switching

### 🎯 Two Operating Modes

#### Practice Mode 🎓
- Red highlights show errors
- Users correct mistakes themselves
- Green animations on correct answers
- Confetti effects for motivation
- Learn while you practice

#### Auto-Correct Mode ⚡
- Automatic error detection and correction
- One-click copy of corrected text
- Perfect for quick fixes
- Replace original text instantly

### 🎨 Visual Effects
- **Green Slide**: Successfully corrected answers
- **Confetti Animation**: Celebrate correct responses
- **Color Transitions**: Smooth red-to-green animations
- **Toast Messages**: Encouraging feedback

## 🚀 How to Use

### Starting the App
1. Launch Grammar Checker
2. Grant overlay permission when prompted
3. Click "Start Grammar Checker"
4. Floating button appears on screen

### Checking Grammar
1. **Select Mode**: Practice or Auto-Correct
2. **Enter Text**: Paste or type your text
3. **Check**: Click "Check Grammar"
4. **Correct**: 
   - Practice: Long-press to enter corrections
   - Auto: Click result to copy corrections

### Language Toggle
- Click "EN/中文" to switch languages
- All interface text changes instantly
- Grammar feedback in chosen language

## 📋 Detected Grammar Errors

| Error Type | Examples Detected |
|------------|-------------------|
| Subject-Verb | "I are", "He have", "They is" |
| Missing Articles | "book is good" → "A book is good" |
| Wrong Prepositions | "good in math" → "good at math" |
| Spelling Errors | "recieve" → "receive" |
| Plural Forms | "childs" → "children" |
| Tense Errors | "I go yesterday" → "I went yesterday" |

## 🛠 Technical Specifications

### Requirements
- **Android Version**: 5.0+ (API 21)
- **Screen Size**: Compatible with all Android devices
- **Storage**: Minimal space required
- **Permissions**: Overlay window permission

### Project Structure
```
GrammarCheckerApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/grammarchecker/
│   │   │   ├── MainActivity.kt          # Main launcher
│   │   │   ├── GrammarCheckActivity.kt  # Core checker
│   │   │   ├── FloatingWindowService.kt # Floating window
│   │   │   ├── GrammarChecker.kt       # Grammar engine
│   │   │   └── AnimationHelper.kt      # UI animations
│   │   └── res/                      # Resources & layouts
│   │       ├── layout/               # UI layouts
│   │       ├── values/               # Strings, colors
│   │       └── drawable/             # Icons & graphics
│   ├── build.gradle                 # App dependencies
│   └── AndroidManifest.xml         # App configuration
├── build.gradle                     # Project configuration
├── settings.gradle                  # Module settings
└── gradle.properties               # Gradle settings
```

## 📦 APK Build Instructions

### Prerequisites
1. **Android Studio** (latest version recommended)
2. **JDK 8** or higher
3. **Android SDK** (API 34 target)

### Build Steps

#### Method 1: Android Studio
1. Open Android Studio
2. Select "Open an existing project"
3. Navigate to `GrammarCheckerApp` folder
4. Wait for Gradle sync to complete
5. Click "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"
6. Find APK in `app/build/outputs/apk/debug/`

#### Method 2: Command Line
```bash
# Open terminal in project directory
cd GrammarCheckerApp

# Make gradlew executable (Linux/Mac)
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# Find APK at: app/build/outputs/apk/debug/app-debug.apk

# For release APK
./gradlew assembleRelease
```

### Install APK
1. Enable "Unknown sources" in Settings → Security
2. Transfer APK to Android device
3. Tap APK file to install
4. Grant required permissions

## 🎯 Sample Usage Session

### Practice Mode Example
```
Input: "I are very happy today"

Step 1: App highlights "are" in red
Step 2: User long-presses result
Step 3: Dialog appears with hint "Enter your correction"
Step 4: User types "am"
Step 5: 🎉 Green animation + confetti + "Correct! Well done!"
Step 6: Text changes to "I am very happy today"
```

### Auto-Correct Mode Example
```
Input: "She have three cats and they was playing"

Step 1: Click "Check Grammar"
Step 2: Result shows:
        "She has three cats and they were playing"
Step 3: Click result → copied to clipboard
Step 4: Paste anywhere with corrections applied
```

## 🔧 Customization

### Adding New Grammar Rules
Edit `GrammarChecker.kt`:
```kotlin
// Add to existing pattern lists
private fun checkSpelling(text: String, errors: MutableList<GrammarError>) {
    val spellingMistakes = listOf(
        "old_error" to "correction",
        // Add new patterns here
        "your_error" to "your_correction"
    )
    // ... rest of function
}
```

### Modifying Animations
Edit `AnimationHelper.kt`:
- Change animation duration
- Modify colors in animations
- Add new animation effects

## 📱 UI Components

### Main Activity
- App introduction and setup
- Floating window permission handling
- Mode selection interface

### Grammar Check Activity
- Text input area
- Result display with color coding
- Mode switching controls
- Language toggle button

### Floating Window
- Toggle button: Opens main checker
- Close button: Stops service
- Draggable: Move to any screen position

## ⚠️ Permissions Explained

### `SYSTEM_ALERT_WINDOW`
- **Purpose**: Display floating window over other apps
- **Grant**: Settings → Apps → Special Access → Draw over other apps
- **Required**: Yes, for core functionality

### Storage Permissions
- **Purpose**: Future file reading/writing features
- **Currently**: Reserved for future use
- **Required**: No, but may be needed for advanced features

## 📊 Features Summary

✅ **Completed Features:**
- Floating window service
- Two operating modes
- Grammar error detection
- Visual error highlighting
- Success animations
- Language switching
- Copy functionality
- Error correction validation
- Confetti effects
- Green slide animations

🚧 **Future Enhancements:**
- Machine learning grammar checking
- More language support
- Advanced error patterns
- Statistics tracking
- Voice input integration

## 🎓 Learning Outcomes

This app teaches users grammar through:
1. **Immediate Feedback**: Red highlights show mistakes instantly
2. **Active Correction**: Users practice fixing errors
3. **Positive Reinforcement**: Animations celebrate success
4. **Pattern Recognition**: Common errors become familiar
5. **Confidence Building**: Success indicators encourage learning

---

**Ready to build!** 🚀

This Grammar Checker app is designed to help users improve their English grammar through interactive practice and immediate feedback. The floating window makes it accessible from any app, while the dual-mode system caters to both learning and productivity needs.