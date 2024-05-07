package com.example.vkproducts.feature.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.vkproducts.core.model.ProductsList


@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val feedContent by remember {
        derivedStateOf {
            uiState.content
        }
    }

    Box(modifier = modifier.fillMaxSize()){
        feedContent?.let { content ->
            Feed(
                modifier = Modifier,
                data = content,
                onBackButtonPressed = {viewModel.previousProductsPage()},
                onForwardButtonPressed = {viewModel.nextProductsPage()}
            )
        }

        uiState.error?.let { error ->
            ErrorSnackBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = error.message,
                onRetry = {viewModel.reloadProducts()}
            )
        }

        LoadingIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp),
            isVisible = {uiState.isLoading}
        )
    }
}

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean = {true}
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible.invoke(),
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary)
                .size(36.dp),
            color = MaterialTheme.colorScheme.inversePrimary
        )
    }
}

@Composable
fun ErrorSnackBar(
    modifier: Modifier = Modifier,
    message: String,
    onRetry: () -> Unit
){
    val density = LocalDensity.current

    val translationY = remember {
        Animatable(100f)
    }

    LaunchedEffect(key1 = true){
        translationY.animateTo(0f)
    }

    Row(
        modifier = modifier
            .graphicsLayer {
                this.translationY = with(density) { translationY.value.dp.toPx() }
            }
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            modifier = Modifier.weight(1f),
            text = message,
            overflow = TextOverflow.Ellipsis,
        )

        IconButton(onClick = onRetry) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "retry load")
        }
    }
}

@Composable
fun Feed(
    modifier: Modifier = Modifier,
    data: ProductsList,
    onBackButtonPressed: () -> Unit,
    onForwardButtonPressed: () -> Unit,
){
    val listState = rememberLazyGridState()

    LaunchedEffect(key1 = data){
        listState.animateScrollToItem(0)
    }

    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = PaddingValues(
            vertical = 20.dp, horizontal = 10.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        columns = GridCells.Fixed(2),
        state = listState
    ){
        item (-1, span = { GridItemSpan(2) }){
            Text(
                modifier = Modifier
                    .padding(bottom = 42.dp),
                text = "VK Products",
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        }

        items(data.products){ products ->
            Product(
                modifier = Modifier.padding(bottom = 12.dp),
                title = products.title,
                description = products.description,
                thumbnail = products.thumbnail,
                price = products.price
            )
        }

        item(data.products.size + 1, span = {GridItemSpan(2)}){
            PagesNav(
                modifier = Modifier.fillMaxWidth(),
                total = data.total, skip = data.skip, limit = data.limit,
                onBackButtonPressed = onBackButtonPressed,
                onForwardButtonPressed = onForwardButtonPressed,
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Product(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    thumbnail: String,
    price: Int
){
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = thumbnail,
                contentScale = ContentScale.Fit,
                contentDescription = "content Image"
            )
        }
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 4.dp, top = 2.dp),
                text = title,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                modifier = Modifier.padding(start = 4.dp, top = 2.dp),
                text = description,
                fontSize = 10.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Normal,
            )

            Text(
                modifier = Modifier
                    .padding(start = 4.dp, top = 2.dp),
                text = "$price $",
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun PagesNav(
    modifier: Modifier = Modifier,
    total: Int,
    skip: Int,
    limit: Int,
    onForwardButtonPressed: () -> Unit = {},
    onBackButtonPressed: () -> Unit = {}
) {

    val currentPage = remember {
        skip / limit + 1
    }

    val countOfPages = remember {
        total / limit + (if (total % limit != 0) 1 else 0)
    }

    val shouldShowArrowBack by remember {
        derivedStateOf {
            skip != 0
        }
    }

    val shouldShowArrowForward by remember {
        derivedStateOf {
            total > skip + limit
        }
    }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.graphicsLayer {
                    alpha = if (shouldShowArrowBack) 1f else 0.3f
                },
                onClick = onBackButtonPressed,
                enabled = shouldShowArrowBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "back"
                )
            }

            Text(
                modifier = Modifier
                    .padding(vertical = 6.dp, horizontal = 12.dp),
                text = currentPage.toString()
            )

            IconButton(
                modifier = Modifier.graphicsLayer {
                    alpha = if (shouldShowArrowForward) 1f else 0.3f
                },
                onClick = onForwardButtonPressed,
                enabled = shouldShowArrowForward
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
                    contentDescription = "forward"
                )
            }
        }

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .alpha(0.5f),
            text = "page $currentPage of $countOfPages",
            fontSize = 12.sp
        )
    }
}