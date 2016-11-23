package cn.com.aetc.aetchome.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cn.com.aetc.aetchome.R;
import cn.com.aetc.aetchome.model.News;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by chenliang3 on 2016/7/30.
 */
public class NewsAdapter extends RecyclerArrayAdapter<News.Stories> {

    private Context context;

    public NewsAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(parent);
    }

    private class NewsViewHolder extends BaseViewHolder<News.Stories>{

        ImageView imageView;
        TextView textView;

        public NewsViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_news);
            imageView = $(R.id.news_item_imageview);
            textView = $(R.id.news_item_textview);
        }

        @Override
        public void setData(News.Stories data) {
            super.setData(data);
            textView.setText(data.getTitle());
            String url = data.getImages().get(0);
            Glide.with(context)
                    .load(url)
                    .crossFade()
                    .centerCrop()
                    .bitmapTransform(new RoundedCornersTransformation(context, 10, 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(imageView);
        }
    }
}
