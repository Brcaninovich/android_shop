package com.brcaninovich.projekat.ItemActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.brcaninovich.projekat.MainApplication.Objects.Artikal;

public class ItemViewModel extends ViewModel {

    private MutableLiveData<Artikal> artikalLiveData = new MutableLiveData<>();
    private int currentImageIndex = 0;

    public void setArtikal(Artikal artikal) {
        artikalLiveData.setValue(artikal);
        setCurrentImageIndex(0); // Resetovanje indeksa slike
    }

    public LiveData<Artikal> getArtikal() {
        return artikalLiveData;
    }

    public int getCurrentImageIndex() {
        return currentImageIndex;
    }

    public void setCurrentImageIndex(int index) {
        currentImageIndex = index;
    }
}
