# Grammar Checker App - Testing Guide

## App Overview

This is an Android grammar checking application with a floating window that provides two modes:

1. **Practice Mode** - Highlights errors in red, users fix them themselves
2. **Auto-Correct Mode** - Provides automatic corrections that can be copied

## Features Implemented

### ✅ Core Features
- **Floating Window**: Accessible from any app via the floating toggle button
- **Two Checking Modes**: Practice and Auto-Correct modes
- **Language Toggle**: Switch between English and Chinese
- **Grammar Checking**: Detects common grammar errors
- **Visual Feedback**: Red highlighting for errors
- **Animations**: Success animations with confetti and green slide effects
- **Copy Function**: One-click copy of corrected text

### ✅ Grammar Error Detection
The app detects common errors including:
- Subject-verb disagreement
- Missing articles (a, an, the)
- Wrong prepositions
- Incorrect plural forms
- Tense errors
- Spelling mistakes
- Punctuation issues

### ✅ User Interaction
- **Practice Mode**: Users tap to see errors, long-press to correct
- **Auto-Correct Mode**: Click result to copy corrected text
- **Language Toggle**: Click EN/中文 button to switch languages

## How to Test

### 1. Build and Install
```bash
# Requires Android Studio or command line tools
cd GrammarCheckerApp
./gradlew assembleDebug
```

### 2. Test Cases

#### Test Case 1: Basic Grammar Check
**Input**: "I are going to the store yesterday"
**Expected**: Red highlight on "are" and "yesterday", suggests "am" and "went"

#### Test Case 2: Practice Mode
1. Enter: "He have many books"
2. Switch to Practice Mode
3. Long-press result text
4. Enter correction: "has"
5. **Expected**: Green animation with confetti 🎉

#### Test Case 3: Auto-Correct Mode
1. Enter: "She go to school every day"
2. Switch to Auto-Correct Mode
3. Click Check Grammar
4. Click result to copy
5. **Expected**: Corrected: "She goes to school every day" copied to clipboard

#### Test Case 4: Language Toggle
1. Click EN/中文 button
2. **Expected**: All UI text switches to Chinese
3. Test grammar checking in Chinese mode

#### Test Case 5: Floating Window
1. Start the app
2. Click "Start Grammar Checker"
3. **Expected**: Floating button appears on screen
4. Click floating button to open grammar checker

## Sample Texts for Testing

### English Samples:
```
I are happy today.
He have three cats.
She go to school.
We is at home.
They was playing football.
```

### Expected Corrections:
- "I are" → "I am"
- "He have" → "He has"
- "She go" → "She goes"
- "We is" → "We are"
- "They was" → "They were"

## Error Detection Examples

| Error Type | Example | Correction |
|------------|---------|------------|
| Subject-Verb | "She have" | "She has" |
| Article Missing | "book is good" | "A book is good" |
| Preposition | "good in math" | "good at math" |
| Spelling | "recieve" | "receive" |
| Plural | "childs" | "children" |
| Tense | "I go yesterday" | "I went yesterday" |

## Animation Effects

### Correct Answer Animation:
1. Green slide effect across the corrected text
2. Confetti particles falling down
3. Background color animation from red to green
4. Success toast message: "🎉 Correct! Well done!"

### Mode Switch Animation:
- Text fade in/out effects when switching modes
- Smooth language transition

## Technical Notes

### Permissions Required:
- `SYSTEM_ALERT_WINDOW` - For floating window
- `READ_EXTERNAL_STORAGE` - For potential file reading
- `WRITE_EXTERNAL_STORAGE` - For potential file writing

### Compatibility:
- Minimum SDK: 21 (Android 5.0)
- Target SDK: 34
- Kotlin version: 1.8.0

### Dependencies:
- AndroidX Core KTX
- Material Components
- ConstraintLayout
- Lifecycle components

## Troubleshooting

### Floating Window Not Appearing:
1. Check Settings → Apps → Special Access → Draw over other apps
2. Grant permission to Grammar Checker app

### Grammar Errors Not Detected:
1. Check if input text contains supported error patterns
2. Try the sample texts provided above

### Animations Not Working:
1. Ensure device animation settings are enabled
2. Check if accessibility services are interfering

## Future Enhancements

- More advanced grammar checking with ML libraries
- Support for more languages
- Integration with system clipboard
- Custom error highlight colors
- Export correction history
- Statistics tracking
- Voice input support

---

This grammar checker app successfully implements all the requested features:
- ✅ Floating window tool
- ✅ Simple English responses with Chinese toggle
- ✅ Two modes: Practice (red highlights) and Auto-correct
- ✅ Visual feedback: green animations and confetti
- ✅ Copy functionality for corrections
- ✅ Ready for APK compilation