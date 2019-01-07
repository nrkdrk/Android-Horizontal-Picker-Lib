package com.erstream.horizontalpicker;

public interface HorizontalPickerListener {
    void onStopDraggingPicker();
    void onDraggingPicker();
    void onDateSelected(Day item);
}
