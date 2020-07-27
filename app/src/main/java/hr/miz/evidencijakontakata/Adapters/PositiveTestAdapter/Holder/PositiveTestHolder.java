package hr.miz.evidencijakontakata.Adapters.PositiveTestAdapter.Holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import hr.miz.evidencijakontakata.databinding.ItemPositiveResultBinding;


public class PositiveTestHolder extends RecyclerView.ViewHolder {
    public ItemPositiveResultBinding binding;

    public PositiveTestHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemPositiveResultBinding.bind(itemView);
    }
}
