    package com.example.retroverse.Fragments;

    import android.os.Bundle;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.DefaultItemAnimator;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;

    import com.bumptech.glide.Glide;
    import com.bumptech.glide.load.engine.DiskCacheStrategy;
    import com.example.retroverse.Adapters.ListaArtigosAdapter;
    import com.example.retroverse.Listeners.ListaFavoritosListener;
    import com.example.retroverse.Listeners.RefreshFavoritosAfterActionListener;
    import com.example.retroverse.Models.Artigo;
    import com.example.retroverse.Models.Perfil;
    import com.example.retroverse.R;
    import com.example.retroverse.Singleton.Singleton;
    import com.example.retroverse.Utils.Utils;

    import java.util.ArrayList;

    public class ListaFavoritosFragment extends Fragment implements ListaFavoritosListener, ListaArtigosAdapter.OnItemLikeClickListener, RefreshFavoritosAfterActionListener {

        View view;
        private RecyclerView recyclerViewFavoritos;
        private ListaArtigosAdapter listaFavoritosAdapter;

        ArrayList<Artigo> listaArtigos;
        private Perfil perfil;
        ImageView ivProfileImgListaFavoritos;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            view = inflater.inflate(R.layout.fragment_lista_favoritos, container, false);
            recyclerViewFavoritos = view.findViewById(R.id.recyclerViewFavoritos);
            ivProfileImgListaFavoritos = view.findViewById(R.id.ivProfileImgListaFavoritos);



            Singleton.getInstance(getContext()).setFavoritosListener(this);
            Singleton.getInstance(getContext()).setRefreshFavoritosAfterActionListenerefreshFavoritosAfterActionListener(this);
            Singleton.getInstance(getContext()).getFavoritosAPI(Utils.getToken(getActivity()), getContext());


            perfil = Singleton.getInstance(getContext()).getPerfil();

            Glide.with(this)
                    .load(perfil.getFotoperfil())
                    .placeholder(R.drawable.profile_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivProfileImgListaFavoritos);

            return view;
        }


        public void onRefreshListaFavoritos(ArrayList<Artigo> favoritos) {
                this.listaArtigos = favoritos;
                setAdapter();

        }

        private void setAdapter() {
            if (listaFavoritosAdapter == null) {

                listaFavoritosAdapter = new ListaArtigosAdapter(this.listaArtigos, getActivity(), true);

                listaFavoritosAdapter.setOnItemLikeClickListener(this);
                if (isAdded()) {
                    int columnCount = getResources().getConfiguration().screenWidthDp / 180;
                    if (columnCount < 2) columnCount = 2;

                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), columnCount);
                    recyclerViewFavoritos.setLayoutManager(layoutManager);
                }
                recyclerViewFavoritos.setItemAnimator(new DefaultItemAnimator());
                recyclerViewFavoritos.setAdapter(listaFavoritosAdapter);


            } else {
                listaFavoritosAdapter.updateData(this.listaArtigos);
                listaFavoritosAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemLikeClick(Artigo artigo, int position) {
            if(artigo.isLiked()){ //SE FOR TRUE IRA ELIMINAR O FAVORITO
                Singleton.getInstance(getActivity()).removeFavoritoAPI(Utils.getToken(getActivity()), artigo, getActivity(), true);

            }
        }

        @Override
        public void onRefreshListaFavoritosAfterAction() {
            Singleton.getInstance(getActivity()).getFavoritosAPI(Utils.getToken(getActivity()), getContext());

        }

        /*@Override
        public void onResume() {
            super.onResume();
            // Caso a lista de favoritos seja alterada, é importante atualizar o Adapter
            listaFavoritosAdapter.notifyDataSetChanged();
        }*/
    }
