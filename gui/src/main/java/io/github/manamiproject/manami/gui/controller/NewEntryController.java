package io.github.manamiproject.manami.gui.controller;

///**
// * Shows the window in which a new entry can be created.
// */
//public class NewEntryController implements Observer {
//
//  /**
//   * Current index of the spinner's textfield value.
//   */
//  private int typeIndex = 0;
//
//  private ValidationSupport validationSupport;
//
//
//  /**
//   * Called on Construction
//   */
//  public void initialize() {
//    validationSupport = new ValidationSupport();
//    validationSupport.;
//
//    txtType.setText(AnimeType.TV.getValue());
//    txtEpisodes.focusedProperty().addListener((currentValue, valueBefore, valueAfter) -> {
//      if (!NumberUtils.isParsable(txtEpisodes.getText()) || txtEpisodes.getText().startsWith("-") || "0".equals(txtEpisodes.getText())) {
//        txtEpisodes.setText("1");
//      } else {
//        try {
//          Integer.parseInt(txtEpisodes.getText());
//        } catch (final NumberFormatException e) {
//          txtEpisodes.setText("1");
//        }
//
//        final boolean deactivateDecreaseButton = Integer.parseInt(txtEpisodes.getText()) == 1;
//        btnEpisodeDown.setDisable(deactivateDecreaseButton);
//      }
//
//    });
//
//    txtInfoLink.focusedProperty().addListener((currentValue, valueBefore, valueAfter) -> {
//      if (valueBefore && !valueAfter) {
//        autoFillForm();
//      }
//    });
//  }
//
//
//  /**
//   * Changes the type to the previous entry.
//   */
//  public void typeUp() {
//    if (typeIndex > 0) {
//      typeIndex--;
//      Platform.runLater(() -> txtType.setText(AnimeType.values()[typeIndex].getValue()));
//      checkTypeArrowButtons();
//    }
//  }
//
//
//  /**
//   * Changes the type to the next entry.
//   */
//  public void typeDown() {
//    if (typeIndex < AnimeType.values().length - 1) {
//      typeIndex++;
//      Platform.runLater(() -> txtType.setText(AnimeType.values()[typeIndex].getValue()));
//      checkTypeArrowButtons();
//    }
//  }
//
//
//  /**
//   * Increases the number of episodes by one.
//   */
//  public void increaseEpisodes() {
//    final Integer value = Integer.valueOf(txtEpisodes.getText()) + 1;
//    txtEpisodes.setText((value > 0) ? value.toString() : "1");
//    if (btnEpisodeDown.isDisabled()) {
//      Platform.runLater(() -> btnEpisodeDown.setDisable(false));
//    }
//  }
//
//
//  /**
//   * Decreases the number of episodes by one.
//   */
//  public void decreaseEpisodes() {
//    final Integer value = Integer.valueOf(txtEpisodes.getText()) - 1;
//    txtEpisodes.setText((value > 0) ? value.toString() : "1");
//    if (value == 1) {
//      Platform.runLater(() -> btnEpisodeDown.setDisable(true));
//    }
//  }
//
//
//  /**
//   * Closes the window without saving anything.
//   */
//  public void close() {
//    Platform.runLater(() -> btnCancel.getParent().getScene().getWindow().hide());
//  }
//
//
//  /**
//   * Adds a new entry to the list.
//   */
//  public void add() {
//    final String title = txtTitle.getText().trim();
//    final Integer episodes = Integer.valueOf(txtEpisodes.getText().trim());
//    final InfoLink infoLink = new InfoLink(txtInfoLink.getText().trim());
//    final String location = txtLocation.getText().trim();
//    final String type = txtType.getText().trim();
//    if (validationSupport.getValidationResult().getErrors().size() == 0) {
//      cmdService.executeCommand(new CmdAddAnime(new Anime(title, infoLink).type(AnimeType.findByName(type)).episodes(episodes).location(location),
//          Main.CONTEXT.getBean(Manami.class)));
//      close();
//    }
//  }
//
//
//  /**
//   * Checks which button to activate or deactivate.
//   */
//  private void checkTypeArrowButtons() {
//    final int upperLimit = AnimeType.values().length - 1;
//
//    if (typeIndex == 0 && !btnTypeUp.isDisabled()) {
//      Platform.runLater(() -> btnTypeUp.setDisable(true));
//    } else if (typeIndex > 0 && btnTypeUp.isDisabled()) {
//      Platform.runLater(() -> btnTypeUp.setDisable(false));
//    }
//
//    if (typeIndex == upperLimit && !btnTypeDown.isDisabled()) {
//      Platform.runLater(() -> btnTypeDown.setDisable(true));
//    } else if (typeIndex < upperLimit && btnTypeDown.isDisabled()) {
//      Platform.runLater(() -> btnTypeDown.setDisable(false));
//    }
//  }
//
//
//  /**
//   * Checks whether or not to disable the episode decrease button.
//   */
//  private void checkEpisodeArrowButtons() {
//    final int episodes = Integer.parseInt(txtEpisodes.getText());
//    btnEpisodeUp.setDisable(false);
//
//    if (episodes > 1) {
//      Platform.runLater(() -> btnEpisodeDown.setDisable(false));
//    } else {
//      Platform.runLater(() -> btnEpisodeDown.setDisable(true));
//    }
//  }
//
//
//  /**
//   * Sets the type in the textfield based on the given value.
//   *
//   * @param value Type of the {@link Anime}
//   */
//  private void setTextfieldType(final String value) {
//    String curListElement;
//    for (int index = 0; index < AnimeType.values().length; index++) {
//      curListElement = AnimeType.values()[index].getValue();
//      if (curListElement.equalsIgnoreCase(value)) {
//        typeIndex = index;
//        txtType.setText(curListElement);
//        break;
//      }
//    }
//    checkTypeArrowButtons();
//  }
//
//
//  /**
//   * Checks the currently given value of the textfield and tries to automatically fill out the form.
//   */
//  private void autoFillForm() {
//    AnimeRetrievalTask retrievalService;
//
//    convertUrlIfNecessary();
//
//    final InfoLink infoLink = new InfoLink(txtInfoLink.getText().trim());
//
//    if (infoLink.isValid()) {
//      setDisableAutoCompleteWidgets(true);
//
//      retrievalService = new AnimeRetrievalTask(Main.CONTEXT.getBean(CacheI.class), infoLink);
//      retrievalService.addObserver(this);
//      serviceRepo.startService(retrievalService);
//
//    }
//  }
//
//
//  private void convertUrlIfNecessary() {
//    final Pattern pattern = Pattern.compile("anime\\.php\\?id=[0-9]*");
//    final Matcher matcher = pattern.matcher(txtInfoLink.getText());
//
//    if (matcher.find()) {
//      String id = matcher.group();
//      id = id.replace("anime.php?id=", EMPTY);
//      txtInfoLink.setText("http://myanimelist.net/anime/".concat(id));
//    }
//  }
//
//
//  /**
//   * Enables or disables all widgets on the scene which are filled by autocomplete.
//   *
//   * @param value Disables the component if the value is true and enables them if the value is false.
//   */
//  private void setDisableAutoCompleteWidgets(final boolean value) {
//    Platform.runLater(() -> {
//      txtTitle.setDisable(value);
//      txtEpisodes.setDisable(value);
//      txtInfoLink.setDisable(value);
//      btnAdd.setDisable(value);
//    });
//
//    if (value) {
//      Platform.runLater(() -> {
//        btnTypeUp.setDisable(true);
//        btnTypeDown.setDisable(true);
//        btnEpisodeUp.setDisable(true);
//        btnEpisodeDown.setDisable(true);
//      });
//    } else {
//      checkTypeArrowButtons();
//      checkEpisodeArrowButtons();
//    }
//  }
//
//
//  public void browse() {
//    final Path directory = showBrowseForFolderDialog(Main.CONTEXT.getBean(MainControllerWrapper.class).getMainStage());
//    String location;
//
//    if (directory != null) {
//      if (config.getFile() == null) {
//        location = directory.toAbsolutePath().toString();
//      } else {
//        try {
//          location = config.getFile().getParent().relativize(directory).toString().replace("\\", "/");
//        } catch (final IllegalArgumentException e) {
//          location = directory.toAbsolutePath().toString();
//        }
//      }
//
//      txtLocation.setText(location);
//    }
//  }
//
//
//  @Override
//  public void update(final Observable observable, final Object object) {
//    if (observable == null || object == null) {
//      return;
//    }
//
//    if (observable instanceof AnimeRetrievalTask && object instanceof Anime) {
//      final Anime anime = (Anime) object;
//
//      if (anime != null) {
//        Platform.runLater(() -> {
//          txtTitle.setText(anime.getTitle());
//          txtEpisodes.setText(String.valueOf(anime.getEpisodes()));
//          txtInfoLink.setText(anime.getInfoLink().getUrl());
//          setTextfieldType(anime.getTypeAsString());
//          checkEpisodeArrowButtons();
//        });
//      }
//      setDisableAutoCompleteWidgets(false);
//    }
//  }
//}