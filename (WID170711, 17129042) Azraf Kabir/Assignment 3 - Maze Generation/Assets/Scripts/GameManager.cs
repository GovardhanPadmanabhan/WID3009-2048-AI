using UnityEngine;
using System.Collections;
using System.Text;
using System.IO;

public class GameManager : MonoBehaviour {

	public Maze mazePrefab;

	public Player playerPrefab;

	private Maze mazeInstance;

	private Player playerInstance;

	private void Start () {
		StartCoroutine(BeginGame());
	}
	
	private void Update () {
		if (Input.GetKeyDown(KeyCode.Space)) {
			RestartGame();
		}
		
	}

	private IEnumerator BeginGame () {
		Camera.main.clearFlags = CameraClearFlags.Skybox;
		Camera.main.rect = new Rect(0f, 0f, 1f, 1f);
		mazeInstance = Instantiate(mazePrefab) as Maze;
		IntVector2 startCoordinate = mazeInstance.RandomCoordinates;
		yield return StartCoroutine(mazeInstance.Generate(startCoordinate));
		playerInstance = Instantiate(playerPrefab) as Player;
		playerInstance.SetLocation(mazeInstance.GetCell(startCoordinate));
		Camera.main.clearFlags = CameraClearFlags.Depth;
		Camera.main.rect = new Rect(0f, 0f, 0.5f, 0.5f);
		
		write_eval_csv(mazeInstance.GetDeadEnds());
	}

	private void RestartGame () {
		StopAllCoroutines();
		Destroy(mazeInstance.gameObject);
		if (playerInstance != null) {
			Destroy(playerInstance.gameObject);
		}
		StartCoroutine(BeginGame());
	}

	public void write_eval_csv (int total_dead_ends) {
        var csv = new StringBuilder();
        var results = string.Format("{0}", total_dead_ends);
        csv.AppendLine(results);

        string path = @"Evaluation Results.csv";

        File.WriteAllText(path, csv.ToString());
    }
}