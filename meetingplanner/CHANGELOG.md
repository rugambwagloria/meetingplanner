# Meeting Planner Project - Change Log

## Recent Changes (Commit 81626b7: \"error messages to display immediately\")

### Completed Tasks from TODO.md (Input Validation Errors Fix)

- **Files Modified**: `src/main/java/edu/sc/bse3211/meetingplanner/PlannerInterface.java`
- **Changes**:
  - Added `while (true)` loops with try-catch blocks around `readInt()` calls in `readMonth()`, `readHour()`, and `readDay(String prompt, int month)`.
  - Custom error printing: e.g., \"Invalid month: 13 (must be 1-12)\" using `IllegalArgumentException` messages from `readInt()`.
  - Reprompts user seamlessly without stack traces or app crash.
  - Verified: Compilation successful (`mvn compile`), app runs smoothly.
- **Impact**: User-friendly input handling; exceptions caught locally in UI layer.

### Ongoing/Planned Tasks (TODO_VALIDATION.md & VALIDATION_TODO.md)

- **Month-Aware Day Validation**:
  - **Implemented Partially**: Added `getDaysInMonth(int month)` returning array-based days (non-leap year).
  - **Updated**: `readDay()` now takes `month` param, uses `maxDay = getDaysInMonth(month)` in `readInt(prompt, 1, maxDay, \"day\")`.
  - **Pending**:
    | Step | Description | Status |
    |------|-------------|--------|
    | 1 | Update all `readDay` callers to pass `month` | Pending |
    | 2 | `mvn compile` | Pending |
    | 3 | Test invalid days (e.g., Feb 30) reprompts | Pending |
    | 4 | `mvn test` for assertions | Pending |

### Other Observations

- **Calendar.java Enhancements** (pre-existing but relevant):
  - `checkTimes()`: Month-specific `daysInMonth[]` array validates max days, throws `TimeConflictException` early.
  - No placeholder meetings for invalid dates.
- **Git Status**: Clean working tree, up-to-date with origin/main.
- **Project**: Maven Java app with JUnit tests; focus on meeting scheduling with rooms/people availability.

## Summary of Key Improvements

1. **Error Handling**: Transformed crashing inputs to graceful reprompts.
2. **Validation**: Progressive – basic ranges → month-aware days (ongoing).
3. **User Experience**: Clean console output, no JVM exceptions visible.

Generated on $(date) based on Git history, TODO files, and code inspection.
