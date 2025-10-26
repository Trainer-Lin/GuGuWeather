package ui.place

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R

/*TODO:
        1.获取viewModel实例
        2.重写onCreateView,加载place_fragment布局
        3.重写onViewCreated: 获取recyclerView , 设置LayoutManager , 初始化并绑定adapter , 写liveData观察逻辑

 */
class PlaceFragment: Fragment() {
    val viewModel: PlaceViewModel by viewModels() //延迟初始化viewModel的语法糖 相当于by lazy的使用viewModelProvider
    private lateinit var adapter: PlaceAdapter //适配器

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("FUCK" , "onAttachSuccess")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("FUCK" , "onCreateViewSuccess")
        return inflater.inflate(R.layout.fragment_place , container , false)
    } //加载布局

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("FUCK" , "onViewCreatedSuccess")

        val context = requireContext() //在onViewCreated里获取context的方式
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        Log.d("FUCK" , "${R.id.recyclerView}")

        val searchPlaceEdit: EditText = view.findViewById(R.id.searchPlaceEdit)
        val bgImageView: ImageView = view.findViewById(R.id.bgImageView)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager //配置layoutManager
        Log.d("FUCK" , recyclerView.layoutManager.toString())
        adapter = PlaceAdapter(this , viewModel.placeList)
       // Log.d("FUCK" , recyclerView.adapter?.itemCount.toString())
        recyclerView.adapter = adapter //配置适配器

        searchPlaceEdit.addTextChangedListener(object: TextWatcher{ //要添加这个方法 必须实现TextWatcher接口的三个方法
            @SuppressLint("NotifyDataSetChanged")//IDE自己加的忽略警告的注解
            override fun afterTextChanged(editable: Editable?){  //editable的翻译是用户输入的文本 / 可编辑内容
                val content = editable.toString()
                if (content.isNotEmpty()) {
                    Log.d("FUCK" , "content is not NULL")
                    viewModel.searchPlace(content) //不为空就直接开始查
                } else {
                    Log.d("FUCK" , "content is NULL")
                    recyclerView.visibility = View.GONE  //隐藏搜索列表
                    bgImageView.visibility = View.VISIBLE  //让背景图片显示出来
                    viewModel.placeList.clear() //清空搜索列表
                    adapter.notifyDataSetChanged() //刷新UI数据
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            //重写三个 只实现一个 空实现两个
        })

        viewModel.placeLiveData.observe(viewLifecycleOwner , Observer{
            result ->
            val places = result.getOrNull()
            if(places != null){
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }
            else{
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })

    }

}