    package com.example.retroverse.Fragments;

    import android.os.Bundle;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.DefaultItemAnimator;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

    import com.example.retroverse.Adapters.ListaArtigosAdapter;
    import com.example.retroverse.Listeners.ListaFavoritosListener;
    import com.example.retroverse.Models.Artigo;
    import com.example.retroverse.R;
    import com.example.retroverse.Singleton.Singleton;
    import com.example.retroverse.Utils;

    import java.util.ArrayList;

    public class ListaFavoritosFragment extends Fragment implements ListaFavoritosListener {

        View view;
        private RecyclerView recyclerViewFavoritos;
        private ListaArtigosAdapter listaFavoritosAdapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            view = inflater.inflate(R.layout.fragment_lista_favoritos, container, false);

            recyclerViewFavoritos = view.findViewById(R.id.recyclerViewFavoritos);

            //FavoritoDBHelper dbHelper = new FavoritoDBHelper(getContext());

            //listaFavoritosAdapter = new ListaFavoritosAdapter(favoritoList, getContext());
            //recyclerViewFavoritos.setAdapter(listaFavoritosAdapter);

            Singleton.getInstance(getActivity()).setFavoritosListener(this);
            Singleton.getInstance(getActivity()).getFavoritosAPI(Utils.getToken(getActivity()), getContext());

            return view;
        }


        public void onRefreshListaFavoritos(ArrayList<Artigo> favoritos) {
            if (favoritos != null) {
                setAdapter(favoritos);
            }
        }

        private void setAdapter(ArrayList<Artigo> artigos) {
            if (listaFavoritosAdapter == null) {

                listaFavoritosAdapter = new ListaArtigosAdapter(artigos, getActivity(), true);

                recyclerViewFavoritos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewFavoritos.setItemAnimator(new DefaultItemAnimator());
                recyclerViewFavoritos.setAdapter(listaFavoritosAdapter);


            } else {
                listaFavoritosAdapter.notifyDataSetChanged();
            }
        }

        /*@Override
        public void onResume() {
            super.onResume();
            // Caso a lista de favoritos seja alterada, é importante atualizar o Adapter
            listaFavoritosAdapter.notifyDataSetChanged();
        }*/
    }
