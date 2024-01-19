package me.henriquelluiz.superheroes

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.henriquelluiz.superheroes.model.Hero
import me.henriquelluiz.superheroes.model.HeroesRepository
import me.henriquelluiz.superheroes.ui.theme.SuperheroesTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HeroList(
    heroes: List<Hero>,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val visibleState = remember {
        MutableTransitionState(false).apply { targetState = true }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
        ),
        exit = fadeOut(),
        modifier = modifier
    ) {
        LazyColumn(contentPadding = paddingValues) {
            itemsIndexed(heroes) {index, hero ->
                HeroCard(
                    hero = hero,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .animateEnterExit(
                            enter = slideInVertically(
                                animationSpec = spring(
                                    stiffness = Spring.StiffnessLow,
                                    dampingRatio = Spring.DampingRatioLowBouncy
                                ),
                                initialOffsetY = { it * (index + 1) }
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun HeroCard(hero: Hero, modifier: Modifier = Modifier) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
                .sizeIn(minHeight = 72.dp)
        ) {
            HeroInformation(
                name = hero.nameRes,
                description = hero.descriptionRes,
                modifier = Modifier
                    .background(Color.Transparent)
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            HeroIcon(
                imageOfHero = hero.imageRes,
                name = hero.nameRes
            )
        }
    }
}

@Composable
fun HeroIcon(
    @DrawableRes imageOfHero: Int,
    @StringRes name: Int,
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        Image(
            painter = painterResource(imageOfHero),
            contentDescription = stringResource(name),
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
fun HeroInformation(
    @StringRes name: Int,
    @StringRes description: Int,
    modifier: Modifier = Modifier
) {
    Column (modifier = modifier) {
        Text(
            text = stringResource(name),
            style = MaterialTheme.typography.displaySmall
        )

        Text(
            text = stringResource(description),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeroListPreview() {
    val heroes = HeroesRepository.heroes
    SuperheroesTheme {
        HeroList(heroes = heroes)
    }
}