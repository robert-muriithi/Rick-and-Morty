package dev.robert.rickandmorty.feature.characters.presentation.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import dev.robert.rickandmorty.core.data.dto.LocationDto
import dev.robert.rickandmorty.core.data.dto.OriginDto
import dev.robert.rickandmorty.core.presentation.designsystem.theme.RickAndMortyTheme
import dev.robert.rickandmorty.core.presentation.designsystem.theme.gilroyRegularFont
import dev.robert.rickandmorty.feature.characters.domain.model.Characters


@Composable
fun CharacterItem(character: Characters, /*onCharacterClick: (Characters) -> Unit*/ modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = 1.dp
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            ) {
            CoilImage(
                imageModel = {
                    character.image
                },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        CircularProgressIndicator()
                    }
                },
                failure = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Text(text = "Error")
                    }
                }

            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(3f)
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.h6,
                        fontFamily = gilroyRegularFont,
                        softWrap = true
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Created on ${character.created}",
                    fontStyle = FontStyle.Italic,
                    color = Color.LightGray,
                    fontFamily = gilroyRegularFont,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = character.species,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = gilroyRegularFont
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            when(character.status){
                                "Alive" -> Color.Green
                                "Dead" -> Color.Red
                                else -> Color.Gray
                            }
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = character.status,
                        style = MaterialTheme.typography.body1,
                        fontFamily = gilroyRegularFont,
                        fontSize = 12.sp
                    )
                }

            }
        }
    }
}




@Preview
@Composable
fun CharacterItemPreview() {
    RickAndMortyTheme {
        CharacterItem(
            character = Characters(
                created = "12/2020",
                episode = listOf(
                    "Ep 01",
                    "Ep o2"
                ),
                gender = "Male",
                id = 1,
                image = "vdjhbvjbd",
                locationDto = LocationDto(
                    name = "Space X",
                    url = "www.rick.com"
                ),
                name = "Rick",
                originDto = OriginDto(
                    name = "Space X",
                    url = "www.rick.com"
                ),
                species = "Unknown",
                status = "Alive",
                type = "Human",
                url = "W",
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}